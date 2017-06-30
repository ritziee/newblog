package com.stashya.domain;

import com.datastax.driver.mapping.annotations.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Se.
 */
@Table(name = "se")
public class Se implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    @NotNull
    private String invent;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvent() {
        return invent;
    }

    public Se invent(String invent) {
        this.invent = invent;
        return this;
    }

    public void setInvent(String invent) {
        this.invent = invent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Se se = (Se) o;
        if (se.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), se.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Se{" +
            "id=" + getId() +
            ", invent='" + getInvent() + "'" +
            "}";
    }
}
