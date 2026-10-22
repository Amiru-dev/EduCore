package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.ValidationException;
import lk.educore.schoolms.service.AcademicService;

import java.util.Objects;

/**
 * Exam Result Domain Model
 * Represents a student's examination score, grade, and evaluation in EduCore.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class ExamResult {

    private int resultId;
    private int examId;
    private String studentId;
    private String subjectName;
    private double marksObtained;
    private double maxMarks;
    private String gradeLetter;
    private String remarks;

    public ExamResult() {
        this.maxMarks = 100.0;
    }

    public ExamResult(int resultId, int examId, String studentId, String subjectName,
                      double marksObtained, double maxMarks) throws ValidationException {
        this.resultId = resultId;
        this.examId = examId;
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
        this.gradeLetter = AcademicService.calculateGrade(marksObtained, maxMarks);
    }

    public double calculatePercentage() throws ValidationException {
        return AcademicService.calculatePercentage(marksObtained, maxMarks);
    }

    public void updateGrade() throws ValidationException {
        this.gradeLetter = AcademicService.calculateGrade(marksObtained, maxMarks);
    }

    public double getGradePoint() {
        return AcademicService.calculateGradePoint(gradeLetter);
    }

    public boolean isPassed() {
        return AcademicService.isPassingGrade(gradeLetter);
    }

    // Getters and Setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getExamId() { return examId; }
    public void setExamId(int examId) { this.examId = examId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }

    public double getMaxMarks() { return maxMarks; }
    public void setMaxMarks(double maxMarks) { this.maxMarks = maxMarks; }

    public String getGradeLetter() { return gradeLetter; }
    public void setGradeLetter(String gradeLetter) { this.gradeLetter = gradeLetter; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamResult that = (ExamResult) o;
        return examId == that.examId && Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examId, studentId);
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "examId=" + examId +
                ", studentId='" + studentId + '\'' +
                ", subject='" + subjectName + '\'' +
                ", marks=" + marksObtained + "/" + maxMarks +
                ", grade='" + gradeLetter + '\'' +
                '}';
    }
}
