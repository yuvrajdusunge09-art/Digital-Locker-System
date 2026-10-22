package com.parcellock;

import java.util.*;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final LockerService service = new LockerService(new FileStorage("parcels.txt"));

    public static void main(String[] args) {
        Thread expiryThread = new Thread(new ExpiryChecker(service), "expiry-checker");
        expiryThread.setDaemon(true);
        expiryThread.start();

        System.out.println("=== PARCEL LOCKER MANAGEMENT SYSTEM ===");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Deposit parcel\n2. Collect parcel\n3. Show parcels\n4. Show lockers\n5. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> deposit();
                    case "2" -> collect();
                    case "3" -> service.showParcels();
                    case "4" -> service.showLockers();
                    case "5" -> running = false;
                    default -> System.out.println("Please enter 1 to 5.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Thank you!");
    }

    private static void deposit() throws Exception {
        System.out.print("Recipient name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        System.out.print("Parcel size (S/M/L): ");
        ParcelSize size = ParcelSize.fromInput(sc.nextLine());
        Parcel p = service.deposit(name, size);
        System.out.println("\nParcel deposited successfully!");
        System.out.println("Parcel ID: " + p.getId());
        System.out.println("Locker: " + p.getLockerId());
        System.out.println("Pickup code: " + p.getPickupCode());
        System.out.println("Keep this pickup code safe.");
    }

    private static void collect() throws Exception {
        System.out.print("Parcel ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Pickup code: ");
        String code = sc.nextLine().trim();
        service.collect(id, code);
        System.out.println("Parcel collected successfully.");
    }
}
