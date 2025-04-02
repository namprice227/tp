package seedu.address.model.person;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a date and time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateTime(String)}
 */
public class DateTime implements Comparable<DateTime> {
    public static final String MESSAGE_CONSTRAINTS = "DateTime should be in the format DD-MM-YYYY HH:MM";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDateTime dateTime;

    /**
     * Constructs a {@code DateTime}.
     *
     * @param dateTime A valid date time string.
     */
    public DateTime(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime, FORMATTER);
    }

    /**
     * Returns true if a given string is a valid date time.
     */
    public static boolean isValidDateTime(String test) {
        try {
            LocalDateTime.parse(test, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks whether a given date-time string represents a future point in time.
     *
     * @param test the date-time string to evaluate
     * @return true if the parsed date-time is after the current system time, false otherwise
     */
    public static boolean isDateTimeFuture(String test) {
        try {
            LocalDateTime inputTime = LocalDateTime.parse(test, FORMATTER);
            return inputTime.isAfter(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public Duration difference(DateTime other) {
        return Duration.between(dateTime, other.dateTime);
    }

    /**
     * Compares two DateTime objects based on chronological order.
     */
    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }


    /**
     * Formats the DateTime object as a string.
     */
    @Override
    public String toString() {
        return dateTime.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DateTime)) {
            return false;
        }

        DateTime otherDateTime = (DateTime) other;
        return dateTime.equals(otherDateTime.dateTime);
    }

    @Override
    public int hashCode() {
        return dateTime.hashCode();
    }

    @Override
    public int compareTo(DateTime other) {
        return this.dateTime.compareTo(other.dateTime);
    }
}
