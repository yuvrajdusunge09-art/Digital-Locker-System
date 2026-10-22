package com.parcellock;
public class MediumLocker extends Locker {
    public MediumLocker(String id) { super(id); }
    @Override public ParcelSize getSize() { return ParcelSize.MEDIUM; }
}
