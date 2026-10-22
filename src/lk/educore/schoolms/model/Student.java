package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.validate.DataValidator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Student Domain Model
 * Represents a student in the EduCore School Management System.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class Student {

    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String nic;
    private String dateOfBirth; // YYYY-MM-DD
    private String grade;
    private String address;
    private String status; // "Active", "Inactive", "Suspended", "Graduated"

    public Student() {
        this.status = "Active";
    }

    public Student(String studentId, String firstName, String lastName, String email, String phone,
                   String nic, String dateOfBirth, String grade, String address) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.nic = nic;
        this.dateOfBirth = dateOfBirth;
        this.grade = grade;
        this.address = address;
        this.status = "Active";
    }

    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName.trim();
        if (lastName == null) return firstName.trim();
        return firstName.trim() + " " + lastName.trim();
    }

    /**
     * Calculate student's age based on date of birth.
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
     * Validate all student fields against EduCore business rules.
     */
    public boolean validate() throws ValidationException {
        DataValidator.isRequired(studentId, "Student ID");
        DataValidator.isValidStudentID(studentId);

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

        DataValidator.isRequired(grade, "Grade");
        return true;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", grade='" + grade + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
