package com.parcellock;

public abstract class Locker {
    private final String id;
    private boolean occupied;
    private String parcelId = "";

    protected Locker(String id) { this.id = id; }
    public String getId() { return id; }
    public boolean isOccupied() { return occupied; }
    public String getParcelId() { return parcelId; }
    public abstract ParcelSize getSize();

    public boolean canFit(ParcelSize size) {
        return !occupied && getSize().ordinal() >= size.ordinal();
    }

    public void assign(String id) {
        if (occupied) throw new IllegalStateException("Locker is already occupied.");
        occupied = true;
        parcelId = id;
    }

    public void release() {
        occupied = false;
        parcelId = "";
    }

    @Override
    public String toString() {
        return id + " | " + getSize() + " | " + (occupied ? "OCCUPIED" : "AVAILABLE");
    }
}
