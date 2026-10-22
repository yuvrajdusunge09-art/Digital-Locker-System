package com.parcellock;

public enum ParcelSize {
    SMALL, MEDIUM, LARGE;

    public static ParcelSize fromInput(String input) {
        return switch (input.trim().toUpperCase()) {
            case "S", "SMALL" -> SMALL;
            case "M", "MEDIUM" -> MEDIUM;
            case "L", "LARGE" -> LARGE;
            default -> throw new IllegalArgumentException("Size must be S, M, or L.");
        };
    }
}
