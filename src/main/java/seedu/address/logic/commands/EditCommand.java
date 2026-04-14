package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVAILABLE_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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
import seedu.address.model.person.Age;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.StartDate;
import seedu.address.model.person.availableday.AvailableDay;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_EMERGENCY_CONTACT + "EMERGENCY_CONTACT] "
            + "[" + PREFIX_ADD_TAG + "TAG]... "
            + "[" + PREFIX_DELETE_TAG + "TAG]... "
            + "[" + PREFIX_AVAILABLE_DAY + "AVAILABLE_DAY]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(formatAthleteMessage(editedPerson));
    }

    /**
     * Returns a formatted success message for the specified {@code Person}.
     *
     * @param athlete The athlete whose details are to be included in the success message.
     * @return A formatted success message containing the athlete's details.
     */
    public static String formatAthleteMessage(Person athlete) {
        String tags = athlete.getTags().isEmpty() ? "-" : athlete.getTags().toString();
        String availableDays = athlete.getAvailableDays().isEmpty() ? "-" : athlete.getAvailableDays().toString();

        return String.format(
                "Athlete edited:%n"
                        + "  Name: %s%n"
                        + "  Age: %s%n"
                        + "  Phone: %s%n"
                        + "  Email: %s%n"
                        + "  Address: %s%n"
                        + "  Emergency Contact: %s%n"
                        + "  Start Date: %s%n"
                        + "  Tags: %s%n"
                        + "  Available Days: %s",
                athlete.getName(),
                athlete.getAge(),
                athlete.getPhone(),
                athlete.getEmail(),
                athlete.getAddress(),
                athlete.getEmergencyContact(),
                athlete.getStartDate(),
                tags,
                availableDays
        );
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
        Age updatedAge = editPersonDescriptor.getAge().orElse(personToEdit.getAge());
        StartDate updatedStartDate = editPersonDescriptor.getStartDate().orElse(personToEdit.getStartDate());
        EmergencyContact updatedEmergencyContact =
                editPersonDescriptor.getEmergencyContact().orElse(personToEdit.getEmergencyContact());

        // Specially for updating tags, need to get the tag of the edited person first
        Set<Tag> currentTags = new HashSet<>(personToEdit.getTags());
        Set<Tag> tagsToAdd = editPersonDescriptor.getTagsToAdd().orElse(Collections.emptySet());
        Set<Tag> tagsToDelete = editPersonDescriptor.getTagsToDelete().orElse(Collections.emptySet());

        Set<Tag> updatedTags = currentTags;
        for (Tag tag : tagsToAdd) {
            updatedTags.add(tag);
        }
        for (Tag tag : tagsToDelete) {
            updatedTags.remove(tag);
        }

        Set<AvailableDay> updatedAvailableDays = editPersonDescriptor.getAvailableDays()
                .orElse(personToEdit.getAvailableDays());

        Person editedPerson = new Person(updatedName, updatedAge, updatedPhone, updatedEmail, updatedAddress,
                updatedEmergencyContact, updatedStartDate, updatedTags, updatedAvailableDays);
        editedPerson.setRunTimings(personToEdit.getRunTimings());
        return editedPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
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
        private Age age;
        private Phone phone;
        private Email email;
        private Address address;
        private StartDate startDate;
        private EmergencyContact emergencyContact;
        private Set<Tag> tagsToAdd;
        private Set<Tag> tagsToDelete;
        private Set<AvailableDay> availableDays;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setAge(toCopy.age);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setStartDate(toCopy.startDate);
            setEmergencyContact(toCopy.emergencyContact);
            setTagsToAdd(toCopy.tagsToAdd);
            setTagsToDelete(toCopy.tagsToDelete);
            setAvailableDays(toCopy.availableDays);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, age, phone, email, address, startDate, emergencyContact,
                tagsToAdd, tagsToDelete, availableDays);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setAge(Age age) {
            this.age = age;
        }

        public Optional<Age> getAge() {
            return Optional.ofNullable(age);
        }

        public void setStartDate(StartDate startDate) {
            this.startDate = startDate;
        }

        public Optional<StartDate> getStartDate() {
            return Optional.ofNullable(startDate);
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

        public void setEmergencyContact(EmergencyContact emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public Optional<EmergencyContact> getEmergencyContact() {
            return Optional.ofNullable(emergencyContact);
        }

        public void setTagsToAdd(Set<Tag> tagsToAdd) {
            if (this.tagsToAdd == null) {
                this.tagsToAdd = new HashSet<Tag>();
            }
            if (tagsToAdd != null && !tagsToAdd.isEmpty()) {
                for (Tag tag : tagsToAdd) {
                    this.tagsToAdd.add(tag);
                }
            }
        }

        public void setTagsToDelete(Set<Tag> tagsToDelete) {
            if (this.tagsToDelete == null) {
                this.tagsToDelete = new HashSet<Tag>();
            }
            if (tagsToDelete != null && !tagsToDelete.isEmpty()) {
                for (Tag tag : tagsToDelete) {
                    this.tagsToDelete.add(tag);
                }
            }
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTagsToAdd() {
            return (tagsToAdd != null) ? Optional.of(Collections.unmodifiableSet(tagsToAdd)) : Optional.empty();
        }

        public Optional<Set<Tag>> getTagsToDelete() {
            return (tagsToDelete != null) ? Optional.of(Collections.unmodifiableSet(tagsToDelete)) : Optional.empty();
        }

        public void setAvailableDays(Set<AvailableDay> availableDays) {
            this.availableDays = (availableDays != null) ? new HashSet<>(availableDays) : null;
        }

        public Optional<Set<AvailableDay>> getAvailableDays() {
            return (availableDays != null) ? Optional.of(Collections.unmodifiableSet(availableDays)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(age, otherEditPersonDescriptor.age)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(startDate, otherEditPersonDescriptor.startDate)
                    && Objects.equals(emergencyContact, otherEditPersonDescriptor.emergencyContact)
                    && Objects.equals(tagsToAdd, otherEditPersonDescriptor.tagsToAdd)
                    && Objects.equals(tagsToDelete, otherEditPersonDescriptor.tagsToDelete)
                    && Objects.equals(availableDays, otherEditPersonDescriptor.availableDays);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("emergencyContact", emergencyContact)
                    .add("tagsToAdd", tagsToAdd)
                    .add("tagsToDelete", tagsToDelete)
                    .add("availableDays", availableDays)
                    .toString();
        }
    }
}
