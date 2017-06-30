package com.stashya.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A K.
 */
@Table(name = "k")
public class K implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        K k = (K) o;
        if (k.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), k.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "K{" +
            "id=" + getId() +
            "}";
    }
}
