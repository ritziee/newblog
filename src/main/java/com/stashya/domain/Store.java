package com.stashya.domain;

import com.datastax.driver.mapping.annotations.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Store.
 */
@Table(name = "store")
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    @NotNull
    private String publishers;

    private String history;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPublishers() {
        return publishers;
    }

    public Store publishers(String publishers) {
        this.publishers = publishers;
        return this;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public String getHistory() {
        return history;
    }

    public Store history(String history) {
        this.history = history;
        return this;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Store store = (Store) o;
        if (store.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + getId() +
            ", publishers='" + getPublishers() + "'" +
            ", history='" + getHistory() + "'" +
            "}";
    }
}
