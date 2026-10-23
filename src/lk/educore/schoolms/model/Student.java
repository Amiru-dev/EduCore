package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.validate.DataValidator;

/**
 * Student Domain Model — extends {@link Person}.
 * <p>
 * Inherits common personal attributes (name, email, phone, NIC, DOB, address,
 * status) from the {@link Person} abstract class and adds student-specific
 * fields: student ID and grade. Overrides the three abstract methods defined by
 * {@link Person} to provide student-specific identity, validation, and role
 * description (polymorphism).
 * </p>
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class Student extends Person {

    // Student-specific fields (not shared with Teacher)
    private String studentId;
    private String grade;

    public Student() {
        super();
        setStatus("Active");
    }

    public Student(String studentId, String firstName, String lastName, String email, String phone,
            String nic, String dateOfBirth, String grade, String address) {
        super(firstName, lastName, email, phone, nic, dateOfBirth, address, "Active");
        this.studentId = studentId;
        this.grade = grade;
    }

    // -------------------------------------------------------------------------
    // Abstract method implementations (polymorphism)
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} For a Student, the unique identifier is the student ID.
     */
    @Override
    public String getPersonId() {
        return studentId;
    }

    /**
     * {@inheritDoc} Validates student-specific fields in addition to the common
     * fields validated by {@link Person#validateCommonFields()}.
     */
    @Override
    public boolean validate() throws ValidationException {
        DataValidator.isRequired(studentId, "Student ID");
        DataValidator.isValidStudentID(studentId);

        // Delegate common field validation to the superclass
        validateCommonFields();

        DataValidator.isRequired(grade, "Grade");
        return true;
    }

    /**
     * {@inheritDoc} Returns a description like "Student – Grade 10A".
     */
    @Override
    public String getRoleDescription() {
        return "Student – Grade " + (grade != null ? grade : "N/A");
    }

    // -------------------------------------------------------------------------
    // Student-specific getters & setters
    // -------------------------------------------------------------------------
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{"
                + "studentId='" + studentId + '\''
                + ", fullName='" + getFullName() + '\''
                + ", grade='" + grade + '\''
                + ", status='" + getStatus() + '\''
                + '}';
    }
}
