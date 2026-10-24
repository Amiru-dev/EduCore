package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.validate.DataValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Abstract base class representing a person in the EduCore School Management
 * System.
 * <p>
 * Demonstrates OOP abstraction and inheritance — common personal attributes are
 * defined once here and reused by {@link Student} and {@link Teacher}
 * subclasses. Subclasses must implement {@link #getPersonId()} and
 * {@link #validate()} to enforce their own identity and business rules
 * (polymorphism).
 * </p>
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public abstract class Person {

    // Encapsulated common fields — private, accessed through inherited getters/setters
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String nic;
    private String dateOfBirth; // YYYY-MM-DD
    private String gender;
    private String address;
    private String status;

    /**
     * Default no-arg constructor.
     */
    protected Person() {
    }

    /**
     * Constructor for all common person fields.
     *
     * @param firstName first name
     * @param lastName last name
     * @param email email address (may be null)
     * @param phone phone number (may be null)
     * @param nic national identity card number (may be null)
     * @param dateOfBirth date of birth in YYYY-MM-DD format (may be null)
     * @param address residential address (may be null)
     * @param status current status (e.g. "Active", "Inactive")
     */
    protected Person(String firstName, String lastName, String email, String phone,
            String nic, String dateOfBirth, String address, String status) {
        this(firstName, lastName, email, phone, nic, dateOfBirth, null, address, status);
    }

    protected Person(String firstName, String lastName, String email, String phone,
            String nic, String dateOfBirth, String gender, String address, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.nic = nic;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.status = status;
    }

    // -------------------------------------------------------------------------
    // Abstract methods — subclasses MUST provide their own implementation
    // -------------------------------------------------------------------------
    /**
     * Returns the unique identifier for this person (e.g. student ID or NIC).
     * Each subclass defines what constitutes its primary key.
     *
     * @return unique person identifier
     */
    public abstract String getPersonId();

    /**
     * Validates all fields of this person against EduCore business rules.
     * Subclasses extend validation with their own domain-specific rules.
     *
     * @return {@code true} if valid
     * @throws ValidationException if any field fails validation
     */
    public abstract boolean validate() throws ValidationException;

    /**
     * Returns a human-readable summary of this person's role in the system
     * (e.g. "Student – Grade 10A", "Teacher – Mathematics"). Used for display
     * purposes; each subclass provides a meaningful description.
     *
     * @return role description string
     */
    public abstract String getRoleDescription();

    // -------------------------------------------------------------------------
    // Concrete shared method — inherited by both Student and Teacher
    // -------------------------------------------------------------------------
    /**
     * Returns the full name by combining first and last name. Handles
     * null-safety so subclasses never need to repeat this logic.
     *
     * @return full name string
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName.trim();
        }
        if (lastName == null) {
            return firstName.trim();
        }
        return firstName.trim() + " " + lastName.trim();
    }

    /**
     * Calculates age in years from {@code dateOfBirth}. Returns 0 if the date
     * of birth is absent or unparseable.
     *
     * @return age in whole years, or 0 if unavailable
     */
    public int calculateAge() {
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            return 0;
        }
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth.trim());
            return Period.between(dob, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Validates the common personal fields shared by all Person subclasses.
     * Subclasses should call {@code super.validateCommonFields()} from their
     * own {@link #validate()} method before adding domain-specific checks.
     *
     * @throws ValidationException if any common field is invalid
     */
    protected void validateCommonFields() throws ValidationException {
        DataValidator.isRequired(firstName, "First Name");
        DataValidator.isLettersOnly(firstName);

        DataValidator.isRequired(lastName, "Last Name");
        DataValidator.isLettersOnly(lastName);

        if (email != null && !email.trim().isEmpty()) {
            DataValidator.isValidEmail(email);
        }

        if (phone != null && !phone.trim().isEmpty()) {
            DataValidator.isValidPhone(phone);
        }

        if (nic != null && !nic.trim().isEmpty()) {
            DataValidator.isValidNIC(nic);
        }

        if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
            DataValidator.isValidDOB(dateOfBirth);
        }
    }

    // -------------------------------------------------------------------------
    // Getters & Setters (encapsulation)
    // -------------------------------------------------------------------------
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(getPersonId(), person.getPersonId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "id='" + getPersonId() + '\''
                + ", fullName='" + getFullName() + '\''
                + ", status='" + status + '\''
                + '}';
    }
}
