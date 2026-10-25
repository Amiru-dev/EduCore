# JasperReport Templates

This directory contains JasperReport XML source files (.jrxml) for generating reports in the EduCore School Management System.

## Report Templates

The following 14 report templates are included:

1. **StudentReport.jrxml** - Comprehensive student list report
2. **TeacherReport.jrxml** - Teacher details and salary report
3. **FeeReceipt.jrxml** - Individual fee receipt
4. **ResultSheet.jrxml** - Exam result sheet for a specific exam
5. **FeeCollectionReport.jrxml** - Summary of fee collections
6. **AttendanceReport.jrxml** - Student attendance report (last 30 days)
7. **SalaryReport.jrxml** - Teacher salary details
8. **GuardianReport.jrxml** - Guardian/parent information report
9. **ExamScheduleReport.jrxml** - Upcoming exam schedule
11. **LibraryReport.jrxml** - Library inventory report
12. **EventReport.jrxml** - School events and notices
13. **FinanceReport.jrxml** - Financial transactions report
14. **StudentPerformanceReport.jrxml** - Individual student performance
15. **GradeWiseReport.jrxml** - Students list by grade
16. **BookIssueReport.jrxml** - Library book issue/return tracking
17. **PendingFeesReport.jrxml** - Outstanding fees report

## Compiling .jrxml to .jasper

To compile these .jrxml files to .jasper (binary) files, you have two options:

### Option 1: Using JasperReports Studio (Recommended)
1. Download and install JasperReports Studio from Jaspersoft
2. Open each .jrxml file in JasperReports Studio
3. Click "Compile" to generate the .jasper file
4. Save the .jasper file in this directory

### Option 2: Using Command Line
If you have JasperReports library in your classpath, you can compile using:

```bash
java -cp jasperreports.jar net.sf.jasperreports.engine.JasperCompileManager report.jrxml
```

### Option 3: Programmatic Compilation
You can also compile programmatically in your Java code:

```java
import net.sf.jasperreports.engine.JasperCompileManager;

JasperCompileManager.compileReportToFile("path/to/report.jrxml", "path/to/report.jasper");
```

## Required Dependencies

To use these reports, add the following dependencies to your project:

```xml
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.20.6</version>
</dependency>
```

## Database Configuration

Each report template contains SQL queries that reference the database schema defined in `school_management_db.sql`. Ensure your database matches this schema before running reports.

## Customization

You can customize these reports by:
1. Opening .jrxml files in JasperReports Studio
2. Modifying layouts, fonts, colors, and styles
3. Adding parameters for dynamic filtering
4. Creating subreports for complex reporting needs

## Output Formats

JasperReports supports multiple output formats:
- PDF (most common)
- HTML
- Excel (XLS/XLSX)
- Word (DOCX)
- CSV
- PowerPoint (PPTX)

## Integration

Integrate reports into your application using:

```java
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;

// Load the compiled report
JasperReport report = JasperCompileManager.compileReport("report.jrxml");

// Create parameters map
Map<String, Object> params = new HashMap<>();
params.put("parameter_name", value);

// Fill the report with data
JasperPrint print = JasperFillManager.fillReport(report, params, connection);

// Export to PDF
JasperExportManager.exportReportToPdfFile(print, "output.pdf");
```