# EduCore School Management System - Project Documentation

## Project Overview
EduCore is a comprehensive School Management System built with Java Swing, designed to manage all aspects of school administration including student management, teacher management, fees, exams, transport, finance, library, events, and guardians.

## Project Structure

### Package Structure
```
lk.educore.schoolms
├── charts/           # Chart components (PieChart, CurveLineChart)
├── component/        # Custom UI components (RoundPanel)
├── email/            # Email services (SendEmail, SendFileEmail, SendQREmail)
├── exceptions/       # Custom exceptions (DatabaseException, EmailException, ValidationException, BusinessException)
├── gui/              # GUI screens (48+ screens)
├── model/            # Data models (MySQL, SendLogo, QRCodeGenerator, BarcodeGenerator)
├── resources/        # Image resources (icons, logos)
├── theme/            # Look and feel themes (FlatMacLightLaf.properties)
└── validate/         # Validation utilities (DataValidator, JOptionIcon)
```

## Database Configuration

### Database Schema
- File: `school_management_db.sql`
- Tables: 25+ tables in 3NF
- Key tables: user, student, teacher, exam, fee, transport, finance, library, event, guardian

### Encrypted Credentials
- Database credentials are AES-128 encrypted in `MySQL.java`
- Default encryption key: "EduCoreSchool2025" (change in production)
- Use SetupConnection.java to configure database connection

## Build Instructions

### Using NetBeans
1. Open the project in NetBeans
2. Right-click project → Clean and Build
3. Run using: Right-click project → Run

### Using Command Line
```bash
cd C:\Users\ASUS\Documents\EduCore
ant clean
ant jar
```

### Main Class
- Main class: `lk.educore.schoolms.gui.SignIn`
- Application title: "EduCore School Management System"

## Authentication & Roles

### User Roles
1. **Admin** - Full access to all modules
2. **Teacher** - Access to attendance, exam management, student viewing
3. **Student** - Access to personal dashboard, results, fees, attendance

### Default Admin Credentials
- Username: `admin`
- Password: `admin123` (change after first login)
- These are inserted in the database SQL file

## Module Overview

### 1. Login & Setup
- **SignIn.java** - Main login screen with role-based routing
- **SetupConnection.java** - Database connection configuration wizard

### 2. Dashboard
- **Dashboard.java** - Main admin/teacher dashboard with sidebar navigation
- **DashboardPanel.java** - Welcome panel with statistics overview

### 3. Student Management (7 screens)
- **StudentManagement.java** - Main student list with CRUD operations
- **StudentRegistration.java** - New student registration with auto ID generation
- **StudentInformation.java** - Student details management
- **StudentInformationDialog.java** - Dialog for viewing/editing student info
- **StudentAddressView.java** - Address management panel
- **StudentMailDialog.java** - Email composition dialog for students
- **StudentView.java** - Comprehensive student details view

### 4. Teacher Management (5 screens)
- **TeacherManagement.java** - Main teacher list with CRUD operations
- **TeacherInformation.java** - Teacher details management
- **TeacherMailDialog.java** - Email composition dialog for teachers
- **AttendanceFrame.java** - QR code scanner for teacher attendance (OpenCV placeholder)
- **BasicSalaryOptions.java** - Salary configuration dialog

### 5. Fee Management (3 screens)
- **FeeManagement.java** - Main fee management interface
- **ViewFeeRecords.java** - Historical fee records with search/filter
- **FeeReceiptDialog.java** - Fee receipt display and print

### 6. Exam & Results (3 screens)
- **ExamManagement.java** - Exam scheduling and management
- **ResultSheet.java** - Result sheet generation
- **ResultDialog.java** - Individual result viewing

### 7. Transport Management (2 screens)
- **TransportManagement.java** - Vehicle and route management
- **VehicleTypeDialog.java** - Vehicle type configuration

### 8. Academic Performance (1 screen)
- **AcademicPerformance.java** - Performance analytics and charts

### 9. Finance Management (1 screen)
- **FinanceManagement.java** - Financial transactions and reporting

### 10. Events & Notices (1 screen)
- **EventManagement.java** - School events and notice board

### 11. Parents/Guardians (3 screens)
- **GuardianManagement.java** - Guardian list and management
- **GuardianAddressView.java** - Guardian address management
- **GuardianDialog.java** - Guardian details dialog

### 12. Library & Inventory (1 screen)
- **LibraryManagement.java** - Book inventory and issue tracking

### 13. Utility Screens (4 screens)
- **BackupUtility.java** - Database backup and restore
- **AboutDialog.java** - Application information
- **HelpDialog.java** - Help documentation
- **SettingsDialog.java** - Application settings

### 14. Student Dashboard (1 screen)
- **StudentDashboard.java** - Student-specific view with profile, attendance, fees, results

## Key Features

### Security
- AES-128 encrypted database credentials
- Role-based access control
- Password hashing (SHA-256)
- Session management

### Email Integration
- Gmail SMTP integration
- Email with attachments
- QR code generation and email attachment
- Student registration email notification

### QR/Barcode Generation
- Student ID QR codes
- Library book barcodes
- Attendance QR scanning (OpenCV placeholder)

### Validation
- Email validation
- NIC validation (Sri Lankan format)
- Phone number validation
- Student ID format validation
- Required field validation

### Custom UI Components
- FlatLaf modern theme
- Gradient panels
- Rounded corners
- Custom dialogs
- Tabbed interfaces
- Search and filter functionality

## Reports (JasperReports)

### Report Templates
Located in `reports/` directory as .jrxml files:
1. StudentReport.jrxml
2. TeacherReport.jrxml
3. FeeReceipt.jrxml
4. ResultSheet.jrxml
5. FeeCollectionReport.jrxml
6. AttendanceReport.jrxml
7. SalaryReport.jrxml
8. GuardianReport.jrxml
9. ExamScheduleReport.jrxml
10. TransportReport.jrxml
11. LibraryReport.jrxml
12. EventReport.jrxml
13. FinanceReport.jrxml
14. StudentPerformanceReport.jrxml
15. GradeWiseReport.jrxml
16. BookIssueReport.jrxml
17. PendingFeesReport.jrxml

### Compiling Reports
Use JasperReports Studio to compile .jrxml to .jasper files, or compile programmatically.

## Resources

### Icons and Logos
Located in `src/lk/educore/schoolms/resources/icons/`
- logo.png (256x256)
- profile.png (64x64)
- Module icons (32x32 each)
- Notification icons (32x32 each)

## Dependencies

### Required Libraries
- MySQL Connector/J (8.0+)
- JavaMail API (for email)
- FlatLaf (UI theme)
- JasperReports (for reports)
- ZXing (for QR/barcode)
- OpenCV (for QR scanning - optional)

### Adding Dependencies in NetBeans
1. Right-click project → Properties
2. Libraries → Add JAR/Folder
3. Add required JAR files

## Configuration Files

### application.properties
```properties
app.name=EduCore School Management System
app.version=1.0.0
app.vendor=EduCore Software
```

### build.xml
Contains build configuration with manifest attributes for JAR creation.

## Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Add Javadoc comments for public methods
- Keep methods focused and concise

### GUI Guidelines
- Use consistent component sizes
- Apply FlatLaf theme
- Use proper layout managers
- Add proper spacing and padding
- Implement proper error handling

### Database Guidelines
- Use PreparedStatement for all queries
- Close connections, statements, and result sets
- Use transactions for multi-step operations
- Handle SQL exceptions properly

## Troubleshooting

### Database Connection Issues
1. Check MySQL service is running
2. Verify credentials in encrypted format
3. Use SetupConnection.java to reconfigure
4. Check MySQL port (default: 3306)

### Email Issues
1. Enable 2FA on Gmail
2. Generate App Password
3. Update email configuration in code
4. Check internet connection

### Build Issues
1. Clean and rebuild project
2. Check for missing dependencies
3. Verify Java version (Java 8+ required)
4. Check NetBeans project settings

## Team Information

### Development Team
- Sheshan Danuksha (Lead Developer)
- Rashintha Jerom
- Dulasha Perera
- Dulangana Dhamsara
- Yomal Kavinda
- Amiru Upek

### Contact
- Email: sheshandanuksha19@gmail.com
- Support: sheshandanuksha19@gmail.com

## License
Proprietary - Copyright (C) 2025 EduCore Software. All rights reserved.

## Version History
- v1.0.0 - Initial release (2025)
  - Complete module implementation
  - Role-based authentication
  - Email integration
  - QR/barcode generation
  - JasperReports templates
  - Student dashboard