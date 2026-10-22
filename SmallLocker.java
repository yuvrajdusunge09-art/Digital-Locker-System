package com.parcellock;
public class SmallLocker extends Locker {
    public SmallLocker(String id) { super(id); }
    @Override public ParcelSize getSize() { return ParcelSize.SMALL; }
}
