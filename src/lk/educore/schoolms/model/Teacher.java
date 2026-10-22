package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.service.FeeService;
import lk.educore.schoolms.validate.DataValidator;

import java.util.Objects;

/**
 * Teacher Domain Model
 * Represents a teacher in the EduCore School Management System.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class Teacher {

    private String nic;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String teacherType;
    private double basicSalary;
    private double allowance;
    private double bonus;
    private String status; // "Active", "Inactive", "On Leave"

    public Teacher() {
        this.status = "Active";
    }

    public Teacher(String nic, String firstName, String lastName, String email, String phone,
                   String teacherType, double basicSalary, double allowance, double bonus) {
        this.nic = nic;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.teacherType = teacherType;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.bonus = bonus;
        this.status = "Active";
    }

    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName.trim();
        if (lastName == null) return firstName.trim();
        return firstName.trim() + " " + lastName.trim();
    }

    /**
     * Calculate gross salary for the teacher.
     */
    public double calculateGrossSalary() throws ValidationException {
        return FeeService.calculateGrossSalary(basicSalary, allowance, bonus);
    }

    /**
     * Calculate net salary after EPF deduction.
     */
    public double calculateNetSalary(double epfRate) throws ValidationException, BusinessException {
        return FeeService.calculateNetSalary(basicSalary, allowance, bonus, 0.0, epfRate);
    }

    /**
     * Validate teacher fields against EduCore business rules.
     */
    public boolean validate() throws ValidationException {
        DataValidator.isRequired(nic, "NIC");
        DataValidator.isValidNIC(nic);

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

        if (basicSalary < 0) {
            throw new ValidationException("Basic salary cannot be negative", "basicSalary", String.valueOf(basicSalary));
        }

        return true;
    }

    // Getters and Setters
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getTeacherType() { return teacherType; }
    public void setTeacherType(String teacherType) { this.teacherType = teacherType; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(nic, teacher.nic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nic);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "nic='" + nic + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", type='" + teacherType + '\'' +
                ", basicSalary=" + basicSalary +
                '}';
    }
}
