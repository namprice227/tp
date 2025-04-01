package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withInsurances("Basic Health Insurance").withConditions("Hypertension")
            .withAllergies("Peanuts").withAppointment("15-12-2023 10:00")
            .withEmergencyPerson("John Doe", "91234567", "Brother").build();

    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withInsurances("Premium Health Insurance").withConditions("Asthma")
            .withAllergies("Pollen").withAppointment("20-12-2023 14:30")
            .withEmergencyPerson("Jane Smith", "92345678", "Sister").build();

    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street")
            .withInsurances("Basic Health Insurance").withConditions("None").withAllergies("None")
            .withAppointment("10-01-2024 09:00")
            .withEmergencyPerson("Emily Clark", "93456789", "Mother").build();

    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street")
            .withInsurances("Standard Health Insurance").withConditions("Diabetes").withAllergies("None")
            .withAppointment("05-02-2024 16:45")
            .withEmergencyPerson("Michael Brown", "94567890", "Father").build();

    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave")
            .withInsurances("None").withConditions("None").withAllergies("None")
            .withAppointment("18-03-2024 11:15")
            .withEmergencyPerson("Sarah Lee", "95678901", "Spouse").build();

    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withInsurances("Standard Health Insurance").withConditions("None").withAllergies("Gluten")
            .withAppointment("22-04-2024 13:00")
            .withEmergencyPerson("David Wong", "96789012", "Friend").build();

    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street")
            .withInsurances("Basic Health Insurance").withConditions("None").withAllergies("None")
            .withAppointment("03-05-2024 15:30")
            .withEmergencyPerson("Laura White", "97890123", "Cousin").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india")
            .withInsurances("None").withConditions("None").withAllergies("None")
            .withAppointment("12-06-2024 08:30")
            .withEmergencyPerson("Tom Harris", "98901234", "Uncle").build();

    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave")
            .withInsurances("Premium Health Insurance").withConditions("None").withAllergies("Shellfish")
            .withAppointment("19-07-2024 17:00")
            .withEmergencyPerson("Grace Kim", "99012345", "Aunt").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withAppointment("25-08-2024 12:00")
            .withEmergencyPerson("Robert Taylor", "90123456", "Grandfather").build();

    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withInsurances("Premium Health Insurance").withConditions("Asthma").withAllergies("None")
            .withAppointment("01-09-2024 09:45")
            .withEmergencyPerson("Linda Green", "91234567", "Grandmother").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}