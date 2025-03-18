package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test class for Appointment.
 */
public class AppointmentTest {

    @Test
    public void testGetters() {
        DateTime dt = new DateTime("18-03-2025 10:00");
        Appointment appointment = new Appointment(dt, "Meeting");
        assertEquals(dt, appointment.getDateTime());
        assertEquals("Meeting", appointment.getDescription());
    }

    @Test
    public void testCompareTo() {
        DateTime dt1 = new DateTime("18-03-2025 10:00");
        DateTime dt2 = new DateTime("18-03-2025 11:00");
        Appointment appointment1 = new Appointment(dt1, "Meeting");
        Appointment appointment2 = new Appointment(dt2, "Lunch");

        // appointment1 should come before appointment2
        assertTrue(appointment1.compareTo(appointment2) < 0);
        // and vice versa
        assertTrue(appointment2.compareTo(appointment1) > 0);

        // When appointments have the same dateTime, compareTo should return 0.
        Appointment appointment3 = new Appointment(dt1, "Another Meeting");
        assertEquals(0, appointment1.compareTo(appointment3));
    }

    @Test
    public void testEquals() {
        DateTime dt1 = new DateTime("18-03-2025 10:00");
        Appointment appointment1 = new Appointment(dt1, "Meeting");
        Appointment appointment2 = new Appointment(dt1, "Another Meeting");
        // equals is based solely on dateTime (with a special case for both being null)
        assertEquals(appointment1, appointment2);

        DateTime dt2 = new DateTime("18-03-2025 11:00");
        Appointment appointment3 = new Appointment(dt2, "Meeting");
        assertNotEquals(appointment1, appointment3);

        // Test default constructor behavior (dateTime is null)
        Appointment defaultAppointment1 = new Appointment();
        Appointment defaultAppointment2 = new Appointment();
        assertEquals(defaultAppointment1, defaultAppointment2);
    }

    @Test
    public void testToString() {
        DateTime dt = new DateTime("18-03-2025 10:00");
        Appointment appointment = new Appointment(dt, "Meeting");
        // toString() should delegate to DateTime's toString() if dateTime is not null.
        assertEquals(dt.toString(), appointment.toString());

        // For a default Appointment (with null dateTime), toString should return an empty string.
        Appointment defaultAppointment = new Appointment();
        assertEquals("", defaultAppointment.toString());
    }
}
