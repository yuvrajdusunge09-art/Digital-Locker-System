package com.parcellock;

public class ExpiryChecker implements Runnable {
    private final LockerService service;
    public ExpiryChecker(LockerService service) { this.service = service; }
    @Override public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            service.checkExpiry();
            try { Thread.sleep(60_000); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }
}
