package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.availableday.AvailableDay;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 *
 * <p>A {@code Person} stores the identity and contact information of an athlete,
 * along with their associated tags and recorded 2.4km run timings.</p>
 *
 * <p>All identity fields are guaranteed to be present and valid.
 * Timing records are stored internally and exposed through an immutable view.</p>
 */
public class Person {

    /** Identity fields */
    private final Name name;
    private final Age age;
    private final Phone phone;
    private final Email email;

    /** Additional data fields */
    private final Address address;
    private final EmergencyContact emergencyContact;
    private final StartDate startDate;

    /** Tags associated with this person. */
    private final Set<Tag> tags = new HashSet<>();

    /** List of recorded 2.4km run timings for this athlete. */
    private final List<RunTiming> runTimings = new ArrayList<>();

    private final Set<AvailableDay> availableDays = new HashSet<>();

    /**
     * Creates a {@code Person}.
     *
     * @param name The person's name.
     * @param age The person's age.
     * @param phone The person's phone number.
     * @param email The person's email address.
     * @param address The person's address.
     * @param startDate The person's start date.
     * @param tags Tags associated with the person.
     * @param availableDays The days the person is available.
     */
    public Person(Name name, Age age, Phone phone, Email email, Address address,
                  EmergencyContact emergencyContact, StartDate startDate, Set<Tag> tags,
                  Set<AvailableDay> availableDays) {
        requireAllNonNull(name, age, phone, email, address,
                emergencyContact, startDate, tags, availableDays);
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.startDate = startDate;
        this.tags.addAll(tags);
        this.availableDays.addAll(availableDays);
    }

    /**
     * Returns the person's name.
     *
     * @return the person's {@link Name}.
     */
    public Name getName() {
        return name;
    }

    /**
     * Returns the person's age.
     *
     * @return the person's {@link Age}.
     */
    public Age getAge() {
        return age;
    }

    /**
     * Returns the person's phone number.
     *
     * @return the person's {@link Phone}.
     */
    public Phone getPhone() {
        return phone;
    }

    /**
     * Returns the person's email address.
     *
     * @return the person's {@link Email}.
     */
    public Email getEmail() {
        return email;
    }

    /**
     * Returns the person's address.
     *
     * @return the person's {@link Address}.
     */
    public Address getAddress() {
        return address;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    /**
     * Returns the person's start date.
     *
     * @return the person's {@link StartDate}.
     */
    public StartDate getStartDate() {
        return startDate;
    }

    /**
     * Returns an immutable view of the person's tags.
     *
     * <p>Attempts to modify the returned set will result in
     * {@code UnsupportedOperationException}.</p>
     *
     * @return an unmodifiable set of tags.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Set<AvailableDay> getAvailableDays() {
        return Collections.unmodifiableSet(availableDays);
    }

    public boolean hasRunTimings() {
        return !runTimings.isEmpty();
    }

    /**
     * Returns an immutable list of recorded run timings.
     *
     * <p>External modifications to the returned list are prevented.</p>
     *
     * @return an unmodifiable list of {@link RunTiming} records.
     */
    public List<RunTiming> getRunTimings() {
        return Collections.unmodifiableList(runTimings);
    }

    /**
     * Replaces the current run timings with the given list.
     * Used when restoring timings from storage.
     *
     * @param timings The list of {@link RunTiming} records to set.
     */
    public void setRunTimings(List<RunTiming> timings) {
        this.runTimings.clear();
        this.runTimings.addAll(timings);
    }

    /**
     * Adds a new run timing record for the athlete.
     *
     * <p>The timing will be appended to the list of recorded timings.
     * If the timing is the fastest recorded time for the athlete,
     * the method returns {@code true} to indicate a new personal best.</p>
     *
     * @param timing The {@link RunTiming} record to add.
     * @return {@code true} if the timing is the athlete's fastest recorded time,
     *         {@code false} otherwise.
     */
    public boolean addRunTiming(RunTiming timing) {
        double previousBest = getBestTimeForDistance(timing.getDistance());
        runTimings.add(timing);
        return timing.getTotalSeconds() < previousBest;
    }

    /**
     * Deletes and returns the run timing at the specified zero-based index.
     *
     * @param timingIndex Zero-based index of the timing to delete.
     * @return the deleted {@link RunTiming}.
     */
    public RunTiming deleteRunTiming(int timingIndex) {
        return runTimings.remove(timingIndex);
    }

    /**
     * Returns the fastest recorded timing for the given distance.
     *
     * @param distance Distance category.
     * @return Fastest time in seconds, or {@code Double.MAX_VALUE} if none exists.
     */
    public double getBestTimeForDistance(String distance) {
        double bestTime = Double.MAX_VALUE;
        for (RunTiming time : runTimings) {
            if (time.getDistance().equals(distance) && time.getTotalSeconds() < bestTime) {
                bestTime = time.getTotalSeconds();
            }
        }
        return bestTime;
    }

    /**
     * Returns true if both persons have the same phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && age.equals(otherPerson.age)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && startDate.equals(otherPerson.startDate)
                && tags.equals(otherPerson.tags)
                && availableDays.equals(otherPerson.availableDays);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, age, phone, email, address, emergencyContact, startDate, tags);
    }

    /**
     * Returns a string representation of this person.
     *
     * @return a formatted string containing the person's details.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("age", age)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("emergencyContact", emergencyContact)
                .add("start date", startDate)
                .add("tags", tags)
                .add("availableDays", availableDays)
                .toString();
    }
}
