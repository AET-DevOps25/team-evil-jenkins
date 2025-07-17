package userservice;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single availability time slot for a given day. This class is
 * used in an @ElementCollection inside {@link User} so it MUST implement a
 * proper equals and hashCode. Both fields are stored as simple strings to keep
 * the schema flexible and language-agnostic.
 */
@Embeddable
public class AvailabilitySlot implements Serializable {

    private String day;
    private String slot;

    public AvailabilitySlot() {
    }

    public AvailabilitySlot(String day, String slot) {
        this.day = day;
        this.slot = slot;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return Objects.equals(day, that.day) && Objects.equals(slot, that.slot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, slot);
    }
}
