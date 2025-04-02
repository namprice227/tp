package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyPerson;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
                new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                        new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                        getTagSet("Peanuts"), getTagSet("Asthma"), getTagSet("HealthShield"),
                        new Appointment(new DateTime("20-06-2024 16:45"), ""),
                        new EmergencyPerson(new Name("Michael Oliveiro"), new Phone("97654321"),
                                new Relationship("Father"))),

                new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                        new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                        getTagSet("Shellfish"), getTagSet("Diabetes"), getTagSet("MediSave"),
                        new Appointment(new DateTime("12-07-2024 11:00"), ""),
                        new EmergencyPerson(new Name("Sophia Li"), new Phone("92345678"), new Relationship("Mother"))),

                new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                        new Address("Blk 47 Tampines Street 20, #17-35"),
                        getTagSet("Dust"), getTagSet("Hypertension"), getTagSet("GreatCare"),
                        new Appointment(new DateTime("05-08-2024 13:15"), ""),
                        new EmergencyPerson(new Name("Amir Ibrahim"),
                                new Phone("93456789"), new Relationship("Cousin"))),

                new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                        new Address("Blk 45 Aljunied Street 85, #11-31"),
                        getTagSet("Gluten"), getTagSet("None"), getTagSet("LifeProtect"),
                        new Appointment(new DateTime("28-09-2024 10:30"), ""),
                        new EmergencyPerson(new Name("Neha Balakrishnan"), new Phone("94567890"),
                                new Relationship("Spouse")))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
