package com.parcellock;

import java.io.IOException;
import java.util.List;

public interface ParcelStorage {
    List<Parcel> load() throws IOException;
    void save(List<Parcel> parcels) throws IOException;
}
