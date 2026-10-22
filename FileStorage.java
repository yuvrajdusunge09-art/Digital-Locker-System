package com.parcellock;

import java.io.*;
import java.util.*;

public class FileStorage implements ParcelStorage {
    private final File file;
    public FileStorage(String fileName) { file = new File(fileName); }

    @Override public List<Parcel> load() throws IOException {
        List<Parcel> parcels = new ArrayList<>();
        if (!file.exists()) return parcels;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    try { parcels.add(Parcel.fromFileLine(line)); }
                    catch (RuntimeException ex) { System.out.println("Skipping invalid data line."); }
                }
            }
        }
        return parcels;
    }

    @Override public void save(List<Parcel> parcels) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Parcel parcel : parcels) {
                writer.write(parcel.toFileLine());
                writer.newLine();
            }
        }
    }
}
