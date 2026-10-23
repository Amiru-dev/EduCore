package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.service.FeeService;

/**
 * Teacher Domain Model — extends {@link Person}.
 * <p>
 * Inherits common personal attributes (name, email, phone, NIC, DOB, address,
 * status) from the {@link Person} abstract class and adds teacher-specific
 * fields: teacher type and salary components. Overrides the three abstract
 * methods defined by {@link Person} to provide teacher-specific identity,
 * validation, and role description (polymorphism).
 * </p>
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class Teacher extends Person {

    // Teacher-specific fields (not shared with Student)
    private String teacherType;
    private double basicSalary;
    private double allowance;
    private double bonus;

    public Teacher() {
        super();
        setStatus("Active");
    }

    public Teacher(String nic, String firstName, String lastName, String email, String phone,
            String teacherType, double basicSalary, double allowance, double bonus) {
        super(firstName, lastName, email, phone, nic, null, null, "Active");
        this.teacherType = teacherType;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.bonus = bonus;
    }

    // -------------------------------------------------------------------------
    // Abstract method implementations (polymorphism)
    // -------------------------------------------------------------------------
    /**
     * {@inheritDoc} For a Teacher, the unique identifier is the NIC.
     */
    @Override
    public String getPersonId() {
        return getNic();
    }

    /**
     * {@inheritDoc} Validates teacher-specific fields in addition to the common
     * fields validated by {@link Person#validateCommonFields()}.
     */
    @Override
    public boolean validate() throws ValidationException {
        // Delegate common field validation to the superclass
        validateCommonFields();

        if (basicSalary < 0) {
            throw new ValidationException("Basic salary cannot be negative", "basicSalary", String.valueOf(basicSalary));
        }

        return true;
    }

    /**
     * {@inheritDoc} Returns a description like "Teacher – Permanent" or
     * "Teacher – Visiting".
     */
    @Override
    public String getRoleDescription() {
        return "Teacher – " + (teacherType != null ? teacherType : "N/A");
    }

    // -------------------------------------------------------------------------
    // Teacher-specific business methods
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // Teacher-specific getters & setters
    // -------------------------------------------------------------------------
    public String getTeacherType() {
        return teacherType;
    }

    public void setTeacherType(String teacherType) {
        this.teacherType = teacherType;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return "Teacher{"
                + "nic='" + getNic() + '\''
                + ", fullName='" + getFullName() + '\''
                + ", type='" + teacherType + '\''
                + ", basicSalary=" + basicSalary
                + '}';
    }
}
