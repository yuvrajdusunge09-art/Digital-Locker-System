package com.parcellock;

import java.io.IOException;
import java.util.*;

public class LockerService {
    private final List<Locker> lockers = new ArrayList<>();
    private final List<Parcel> parcels = new ArrayList<>();
    private final ParcelStorage storage;

    public LockerService(ParcelStorage storage) {
        this.storage = storage;
        lockers.add(new SmallLocker("S1")); lockers.add(new SmallLocker("S2"));
        lockers.add(new MediumLocker("M1")); lockers.add(new MediumLocker("M2"));
        lockers.add(new LargeLocker("L1")); lockers.add(new LargeLocker("L2"));
        try {
            parcels.addAll(storage.load());
            for (Parcel p : parcels) {
                if (p.getStatus().equals("WAITING") || p.getStatus().equals("EXPIRED")) {
                    findLocker(p.getLockerId()).ifPresent(l -> l.assign(p.getId()));
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    public synchronized Parcel deposit(String recipient, ParcelSize size) throws Exception {
        Locker chosen = lockers.stream().filter(l -> l.canFit(size))
                .min(Comparator.comparingInt(l -> l.getSize().ordinal()))
                .orElseThrow(() -> new LockerUnavailableException("No suitable locker is available."));
        String id = "P" + (1000 + new Random().nextInt(9000));
        while (findParcel(id).isPresent()) id = "P" + (1000 + new Random().nextInt(9000));
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        Parcel parcel = new Parcel(id, recipient, size, chosen.getId(), code);
        chosen.assign(id);
        parcels.add(parcel);
        storage.save(parcels);
        return parcel;
    }

    public synchronized void collect(String id, String code) throws Exception {
        Parcel parcel = findParcel(id).orElseThrow(() -> new ParcelNotFoundException("Parcel ID not found."));
        if (!parcel.getPickupCode().equals(code)) throw new InvalidPickupCodeException("Incorrect pickup code.");
        if (parcel.getStatus().equals("EXPIRED") || parcel.isExpired()) {
            parcel.markExpired(); storage.save(parcels);
            throw new ParcelExpiredException("Parcel expired. Contact the operator.");
        }
        if (!parcel.getStatus().equals("WAITING")) throw new IllegalStateException("Parcel is not awaiting pickup.");
        parcel.markCollected();
        findLocker(parcel.getLockerId()).ifPresent(Locker::release);
        storage.save(parcels);
    }

    public synchronized void checkExpiry() {
        boolean changed = false;
        for (Parcel p : parcels) {
            if (p.isExpired() && p.getStatus().equals("WAITING")) {
                p.markExpired(); changed = true;
            }
        }
        if (changed) try { storage.save(parcels); }
        catch (IOException e) { System.out.println("Could not save expiry changes: " + e.getMessage()); }
    }

    public synchronized void showParcels() {
        if (parcels.isEmpty()) { System.out.println("No parcels yet."); return; }
        parcels.forEach(System.out::println);
    }
    public synchronized void showLockers() { lockers.forEach(System.out::println); }
    private Optional<Parcel> findParcel(String id) {
        return parcels.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst();
    }
    private Optional<Locker> findLocker(String id) {
        return lockers.stream().filter(l -> l.getId().equals(id)).findFirst();
    }
}
