package com.parcellock;
public class LargeLocker extends Locker {
    public LargeLocker(String id) { super(id); }
    @Override public ParcelSize getSize() { return ParcelSize.LARGE; }
}
