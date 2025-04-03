package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient identified "
            + "by the index number used in the displayed patient list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_ALLERGY + "ALLERGY] "
            + "[" + PREFIX_CONDITION + "CONDITION] "
            + "[" + PREFIX_INSURANCE + "INSURANCE]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This patient already exists in HealthSync.";
    public static final String MESSAGE_CONFIRMATION = "Are you sure you want to edit this patient? (y/n)";
    public static final String MESSAGE_NO_CHANGES = "No changes were made to the patient's information.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private boolean needsConfirmation;

    /**
     * Constructs an EditCommand.
     * If no index or fields are specified, it requires confirmation.
     *
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        // Needs confirmation only if fields are not edited
        this.needsConfirmation = editPersonDescriptor.isAnyFieldEdited();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (needsConfirmation) {
            needsConfirmation = false;
            return new CommandResult(MESSAGE_CONFIRMATION, false, false, true);
        }

        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        // If no changes were made, notify the user
        if (personToEdit.equals(editedPerson)) {
            throw new CommandException(MESSAGE_NO_CHANGES);
        }

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());

        Person editedPerson = new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                personToEdit.getTags(), personToEdit.getAppointment(), personToEdit.getEmergencyContact());
        return editedPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof EditCommand otherEditCommand) {
            return index.equals(otherEditCommand.index)
                    && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
        }
        return false;
    }

    public void setConfirmation(boolean confirmation) {
        needsConfirmation = confirmation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private List<Set<Tag>> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets the tags. Ensures the list contains exactly three sets.
         */
        public void setTags(List<Set<Tag>> tags) {
            if (tags == null) {
                this.tags = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    this.tags.add(new HashSet<>());
                }
            } else {
                this.tags = new ArrayList<>(tags.size());
                for (Set<Tag> tagSet : tags) {
                    this.tags.add(new HashSet<>(tagSet));
                }
                // Ensure there are exactly three sets
                while (this.tags.size() < 3) {
                    this.tags.add(new HashSet<>());
                }
            }
        }

        public Optional<List<Set<Tag>>> getTags() {
            return Optional.of(Collections.unmodifiableList(tags));
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (other instanceof EditPersonDescriptor otherDescriptor) {
                return Objects.equals(name, otherDescriptor.name)
                        && Objects.equals(phone, otherDescriptor.phone)
                        && Objects.equals(email, otherDescriptor.email)
                        && Objects.equals(address, otherDescriptor.address);
            }
            return false;
        }

        @Override
        public String toString() {
            return "EditPersonDescriptor{name=" + name + ", phone=" + phone + ", email=" + email
                + ", address=" + address + "}";
        }
    }
}
