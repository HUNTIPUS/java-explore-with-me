package ru.practicum.private_access.events.location.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

@IdClass(LocationKey.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationKey implements Serializable {

    private Float lat;
    private Float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationKey that = (LocationKey) o;
        return Objects.equals(lat, that.lat) && Objects.equals(lon, that.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
