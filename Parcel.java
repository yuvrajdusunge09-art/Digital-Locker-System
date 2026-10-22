package com.parcellock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parcel {
    private final String id;
    private final String recipient;
    private final ParcelSize size;
    private final String lockerId;
    private final String pickupCode;
    private final LocalDateTime depositedAt;
    private String status;

    public Parcel(String id, String recipient, ParcelSize size, String lockerId, String pickupCode) {
        this.id = id;
        this.recipient = recipient;
        this.size = size;
        this.lockerId = lockerId;
        this.pickupCode = pickupCode;
        this.depositedAt = LocalDateTime.now();
        this.status = "WAITING";
    }

    public String getId() { return id; }
    public String getRecipient() { return recipient; }
    public ParcelSize getSize() { return size; }
    public String getLockerId() { return lockerId; }
    public String getPickupCode() { return pickupCode; }
    public String getStatus() { return status; }
    public LocalDateTime getDepositedAt() { return depositedAt; }
    public boolean isExpired() { return depositedAt.plusHours(24).isBefore(LocalDateTime.now()); }
    public void markCollected() { status = "COLLECTED"; }
    public void markExpired() { if (status.equals("WAITING")) status = "EXPIRED"; }

    public String toFileLine() {
        return String.join("|", clean(id), clean(recipient), size.name(), lockerId, pickupCode,
                depositedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), status);
    }
    private String clean(String value) { return value.replace("|", " ").replace("\n", " "); }
    public static Parcel fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        Parcel parcel = new Parcel(p[0], p[1], ParcelSize.valueOf(p[2]), p[3], p[4]);
        parcel.status = p[6];
        return parcel.withTime(LocalDateTime.parse(p[5]));
    }
    private Parcel withTime(LocalDateTime time) {
        // Rebuild timestamp through the internal helper constructor.
        return new Parcel(id, recipient, size, lockerId, pickupCode, time, status);
    }
    private Parcel(String id, String recipient, ParcelSize size, String lockerId, String pickupCode,
                   LocalDateTime time, String status) {
        this.id=id; this.recipient=recipient; this.size=size; this.lockerId=lockerId;
        this.pickupCode=pickupCode; this.depositedAt=time; this.status=status;
    }
    @Override public String toString() {
        return id + " | " + recipient + " | " + size + " | " + lockerId + " | " + status;
    }
}
