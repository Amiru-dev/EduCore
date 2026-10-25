-- School Management System Database
-- EduCore School Management System
-- MySQL Database Schema (3NF)

-- Create Database
CREATE DATABASE IF NOT EXISTS school_management_db;
USE school_management_db;

-- ============================================
-- DROP EXISTING TABLES (in correct order to respect foreign keys)
-- ============================================

-- Drop tables with foreign keys first (children before parents)
DROP TABLE IF EXISTS book_issue;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS finance;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS student_transport;
DROP TABLE IF EXISTS transport_route;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS fee;
DROP TABLE IF EXISTS result;
DROP TABLE IF EXISTS exam;
DROP TABLE IF EXISTS teacher_subject;
DROP TABLE IF EXISTS subject;
DROP TABLE IF EXISTS teacher_class;
DROP TABLE IF EXISTS teacher_leave;
DROP TABLE IF EXISTS teacher_attendance;
DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS student_attendance;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS guardian;

-- Drop lookup tables
DROP TABLE IF EXISTS vehicle_status;
DROP TABLE IF EXISTS vehicle_type;
DROP TABLE IF EXISTS event_status;
DROP TABLE IF EXISTS transaction_type;
DROP TABLE IF EXISTS payment_method;
DROP TABLE IF EXISTS fee_type;
DROP TABLE IF EXISTS leave_type;
DROP TABLE IF EXISTS exam_type;
DROP TABLE IF EXISTS teacher_status;
DROP TABLE IF EXISTS teacher_type;
DROP TABLE IF EXISTS grade;
DROP TABLE IF EXISTS book_category;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS user_type;

-- ============================================
-- USER MANAGEMENT TABLES
-- ============================================

-- User Type Lookup Table
CREATE TABLE IF NOT EXISTS user_type (
    user_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default user types
INSERT IGNORE INTO user_type (type_name, description) VALUES 
('Admin', 'System Administrator with full access'),
('Teacher', 'Teacher with limited access'),
('Student', 'Student with view-only access');

-- User Authentication Table
CREATE TABLE IF NOT EXISTS user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_type_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    FOREIGN KEY (user_type_id) REFERENCES user_type(user_type_id) ON DELETE CASCADE
);

-- ============================================
-- STUDENT MANAGEMENT TABLES
-- ============================================

-- Grade/Class Lookup Table
CREATE TABLE IF NOT EXISTS grade (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    grade_name VARCHAR(50) NOT NULL,
    section VARCHAR(10),
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_grade_section (grade_name, section)
);

-- Insert default grades
INSERT IGNORE INTO grade (grade_name, section) VALUES 
('Grade 6', 'A'), ('Grade 6', 'B'), ('Grade 6', 'C'),
('Grade 7', 'A'), ('Grade 7', 'B'), ('Grade 7', 'C'),
('Grade 8', 'A'), ('Grade 8', 'B'), ('Grade 8', 'C'),
('Grade 9', 'A'), ('Grade 9', 'B'), ('Grade 9', 'C'),
('Grade 10', 'A'), ('Grade 10', 'B'), ('Grade 10', 'C'),
('Grade 11', 'A'), ('Grade 11', 'B'), ('Grade 11', 'C'),
('Grade 12', 'A'), ('Grade 12', 'B'), ('Grade 12', 'C'),
('Grade 13', 'A'), ('Grade 13', 'B'), ('Grade 13', 'C');

-- Guardian/Parent Table
CREATE TABLE IF NOT EXISTS guardian (
    guardian_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    nic VARCHAR(20) UNIQUE,
    relationship VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    occupation VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Student Table
CREATE TABLE IF NOT EXISTS student (
    student_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    nic VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    grade_id INT NOT NULL,
    guardian_id INT NOT NULL,
    user_id INT,
    registration_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE,
    FOREIGN KEY (guardian_id) REFERENCES guardian(guardian_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

-- Student Attendance Table
CREATE TABLE IF NOT EXISTS student_attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    date_time DATETIME NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, attendance_date)
);

-- ============================================
-- TEACHER/STAFF MANAGEMENT TABLES
-- ============================================

-- Teacher Type Lookup Table
CREATE TABLE IF NOT EXISTS teacher_type (
    teacher_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default teacher types
INSERT IGNORE INTO teacher_type (type_name, description) VALUES 
('Teacher', 'Regular Teacher'),
('Principal', 'School Principal'),
('Office Staff', 'Administrative Staff'),
('Support Staff', 'Support Staff');

-- Teacher Status Lookup Table
CREATE TABLE IF NOT EXISTS teacher_status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default teacher statuses
INSERT IGNORE INTO teacher_status (status_name, description) VALUES 
('Active', 'Currently employed'),
('Inactive', 'Not currently employed'),
('On Leave', 'On leave of absence'),
('Retired', 'Retired from service');

-- Teacher/Staff Table
CREATE TABLE IF NOT EXISTS teacher (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    NIC VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    teacher_type_id INT NOT NULL,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    salary DECIMAL(10, 2) NOT NULL,
    hire_date DATE NOT NULL,
    status_id INT NOT NULL,
    user_id INT,
    photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_type_id) REFERENCES teacher_type(teacher_type_id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES teacher_status(status_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

-- Teacher Attendance Table (QR Scanning)
CREATE TABLE IF NOT EXISTS teacher_attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_NIC VARCHAR(20) NOT NULL,
    date_time DATETIME NOT NULL,
    attendance_date DATE NOT NULL,
    check_in TIME NOT NULL,
    check_out TIME,
    duration INT COMMENT 'Duration in minutes',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE CASCADE,
    UNIQUE KEY unique_teacher_attendance (teacher_NIC, attendance_date)
);

-- Leave Type Lookup Table
CREATE TABLE IF NOT EXISTS leave_type (
    leave_type_id INT PRIMARY KEY AUTO_INCREMENT,
    leave_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    max_days INT DEFAULT 30,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default leave types
INSERT IGNORE INTO leave_type (leave_type_name, description, max_days) VALUES 
('Sick Leave', 'Medical leave', 14),
('Casual Leave', 'Personal leave', 7),
('Annual Leave', 'Yearly vacation', 30),
('Maternity Leave', 'Maternity leave', 84),
('Paternity Leave', 'Paternity leave', 7);

-- Teacher Leave Table
CREATE TABLE IF NOT EXISTS teacher_leave (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_NIC VARCHAR(20) NOT NULL,
    leave_type_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    approved_by VARCHAR(100),
    approved_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE CASCADE,
    FOREIGN KEY (leave_type_id) REFERENCES leave_type(leave_type_id) ON DELETE CASCADE
);

-- ============================================
-- EXAM & RESULTS TABLES
-- ============================================

-- Subject Table
CREATE TABLE IF NOT EXISTS subject (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL UNIQUE,
    grade_id INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE
);

-- ============================================
-- TEACHER ↔ CLASS / SUBJECT ASSIGNMENT TABLES
-- ============================================

-- Teacher ↔ Class assignment (which grades a teacher is responsible for)
CREATE TABLE IF NOT EXISTS teacher_class (
    id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_NIC VARCHAR(20) NOT NULL,
    grade_id INT NOT NULL,
    is_class_teacher BOOLEAN DEFAULT FALSE,
    academic_year INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE CASCADE,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE,
    UNIQUE KEY unique_teacher_class (teacher_NIC, grade_id, academic_year)
);

CREATE INDEX idx_teacher_class_nic ON teacher_class(teacher_NIC);
CREATE INDEX idx_teacher_class_grade ON teacher_class(grade_id);

-- Teacher ↔ Subject assignment (which subjects a teacher teaches in a grade)
CREATE TABLE IF NOT EXISTS teacher_subject (
    id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_NIC VARCHAR(20) NOT NULL,
    subject_id INT NOT NULL,
    grade_id INT NOT NULL,
    academic_year INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE,
    UNIQUE KEY unique_teacher_subject (teacher_NIC, subject_id, grade_id, academic_year)
);

CREATE INDEX idx_teacher_subject_nic ON teacher_subject(teacher_NIC);
CREATE INDEX idx_teacher_subject_subject ON teacher_subject(subject_id);

-- Exam Type Lookup Table
CREATE TABLE IF NOT EXISTS exam_type (
    exam_type_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default exam types for Central College Anuradhapura
-- Only term tests that go into report cards
INSERT IGNORE INTO exam_type (exam_type_name, description) VALUES 
('1st Term', 'First term examination'),
('2nd Term', 'Second term examination'),
('3rd Term', 'Third term examination');

-- Exam Table
CREATE TABLE IF NOT EXISTS exam (
    exam_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_name VARCHAR(100) NOT NULL,
    exam_type_id INT NOT NULL,
    subject_id INT NOT NULL,
    grade_id INT NOT NULL,
    exam_date DATE NOT NULL,
    max_marks DECIMAL(5, 2) NOT NULL DEFAULT 100.00,
    duration_minutes INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (exam_type_id) REFERENCES exam_type(exam_type_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE
);

-- Result Table
CREATE TABLE IF NOT EXISTS result (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    exam_id INT NOT NULL,
    marks_obtained DECIMAL(5, 2) NOT NULL,
    grade_letter VARCHAR(5),
    teacher_NIC VARCHAR(20),
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (exam_id) REFERENCES exam(exam_id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE SET NULL,
    UNIQUE KEY unique_result (student_id, exam_id)
);

-- ============================================
-- FEE MANAGEMENT TABLES
-- ============================================

-- Fee Type Lookup Table
CREATE TABLE IF NOT EXISTS fee_type (
    fee_type_id INT PRIMARY KEY AUTO_INCREMENT,
    fee_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    default_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert Central College Anuradhapura specific fee types
-- Government national school with no tuition fees
INSERT IGNORE INTO fee_type (fee_type_name, description, default_amount) VALUES 
('SDS Contribution', 'School Development Society contribution', 1000.00),
('Society/Club Fee', 'Society and club membership fee', 500.00),
('Trip Charge', 'Educational trip charges', 1500.00),
('Library Fine', 'Library fine for overdue books', 50.00),
('Lost Book Charge', 'Charge for lost library books', 1000.00);

-- Fee Table
CREATE TABLE IF NOT EXISTS fee (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    fee_type_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    due_date DATE NOT NULL,
    paid_date DATE,
    status ENUM('Pending', 'Paid', 'Overdue', 'Cancelled') DEFAULT 'Pending',
    receipt_no VARCHAR(50) UNIQUE,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (fee_type_id) REFERENCES fee_type(fee_type_id) ON DELETE CASCADE
);

-- ============================================

-- EVENTS & NOTICES TABLES
-- ============================================

-- Event Status Lookup Table
CREATE TABLE IF NOT EXISTS event_status (
    event_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default event statuses
INSERT IGNORE INTO event_status (status_name, description) VALUES 
('Active', 'Upcoming event'),
('Completed', 'Event completed'),
('Cancelled', 'Event cancelled'),
('Postponed', 'Event postponed');

-- Event Table
CREATE TABLE IF NOT EXISTS event (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    event_status_id INT NOT NULL,
    created_by VARCHAR(100),
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_status_id) REFERENCES event_status(event_status_id) ON DELETE CASCADE
);

-- ============================================
-- FINANCE TABLES
-- ============================================

-- Transaction Type Lookup Table
CREATE TABLE IF NOT EXISTS transaction_type (
    transaction_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default transaction types
INSERT IGNORE INTO transaction_type (type_name, description) VALUES 
('Income', 'Income transaction'),
('Expense', 'Expense transaction');

-- Finance Table
CREATE TABLE IF NOT EXISTS finance (
    finance_id INT PRIMARY KEY AUTO_INCREMENT,
    description TEXT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    transaction_type_id INT NOT NULL,
    transaction_date DATE NOT NULL,
    category VARCHAR(100),
    added_by VARCHAR(100),
    reference_no VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(transaction_type_id) ON DELETE CASCADE
);

-- ============================================
-- LIBRARY & INVENTORY TABLES
-- ============================================

-- Book Category Lookup Table
CREATE TABLE IF NOT EXISTS book_category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default book categories
INSERT IGNORE INTO book_category (category_name, description) VALUES 
('Fiction', 'Fiction books'),
('Science', 'Science books'),
('Textbook', 'Textbooks'),
('Reference', 'Reference books'),
('History', 'History books'),
('Biography', 'Biographies'),
('Technology', 'Technology books');

-- Book Table
CREATE TABLE IF NOT EXISTS book (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    category_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    available_qty INT NOT NULL DEFAULT 1,
    publisher VARCHAR(100),
    publication_year INT,
    price DECIMAL(10, 2),
    location VARCHAR(50),
    barcode VARCHAR(50) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES book_category(category_id) ON DELETE CASCADE
);

-- Book Issue Table
CREATE TABLE IF NOT EXISTS book_issue (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine DECIMAL(10, 2) DEFAULT 0.00,
    status ENUM('Issued', 'Returned', 'Overdue') DEFAULT 'Issued',
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES book(book_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE
);

-- ============================================
-- INDEXES FOR PERFORMANCE
-- ============================================

-- Student indexes
CREATE INDEX idx_student_grade ON student(grade_id);
CREATE INDEX idx_student_guardian ON student(guardian_id);
CREATE INDEX idx_student_active ON student(is_active);

-- Teacher indexes
CREATE INDEX idx_teacher_type ON teacher(teacher_type_id);
CREATE INDEX idx_teacher_status ON teacher(status_id);

-- Exam indexes
CREATE INDEX idx_exam_subject ON exam(subject_id);
CREATE INDEX idx_exam_grade ON exam(grade_id);
CREATE INDEX idx_exam_date ON exam(exam_date);

-- Result indexes
CREATE INDEX idx_result_student ON result(student_id);
CREATE INDEX idx_result_exam ON result(exam_id);

-- Fee indexes
CREATE INDEX idx_fee_student ON fee(student_id);
CREATE INDEX idx_fee_type ON fee(fee_type_id);
CREATE INDEX idx_fee_status ON fee(status);
CREATE INDEX idx_fee_date ON fee(due_date);

-- Attendance indexes
CREATE INDEX idx_student_attendance_student ON student_attendance(student_id);
CREATE INDEX idx_student_attendance_date ON student_attendance(attendance_date);
CREATE INDEX idx_teacher_attendance_teacher ON teacher_attendance(teacher_NIC);
CREATE INDEX idx_teacher_attendance_date ON teacher_attendance(attendance_date);

-- ============================================
-- SAMPLE DATA FOR TESTING
-- ============================================

-- Insert sample admin user (password: admin123)
INSERT IGNORE INTO user (username, password, email, user_type_id) VALUES 
('admin', 'admin123', 'admin@educore.lk', 1);

-- ============================================
-- SAMPLE GUARDIANS (Sri Lankan)
-- ============================================

INSERT IGNORE INTO guardian (first_name, last_name, nic, relationship, phone, email, address, occupation) VALUES 
('Kumara', 'Bandara', '751234567V', 'Father', '0712345678', 'kumara.bandara@gmail.com', 'No. 45, Main Street, Anuradhapura', 'Teacher'),
('Priyanka', 'Silva', '801234567V', 'Mother', '0723456789', 'priyanka.silva@yahoo.com', 'No. 23, School Road, Anuradhapura', 'Banker'),
('Saman', 'Perera', '781234567V', 'Father', '0734567890', 'saman.perera@hotmail.com', 'No. 67, New Town, Anuradhapura', 'Engineer'),
('Fathima', 'Mohamed', '851234567V', 'Mother', '0745678901', 'fathima.mohamed@gmail.com', 'No. 12, Mosque Road, Anuradhapura', 'Doctor'),
('Nimal', 'Fernando', '771234567V', 'Father', '0756789012', 'nimal.fernando@sltnet.lk', 'No. 89, Hospital Road, Anuradhapura', 'Lawyer'),
('Keshara', 'Jayasinghe', '821234567V', 'Mother', '0767890123', 'keshara.jayasinghe@gmail.com', 'No. 34, College Avenue, Anuradhapura', 'Accountant'),
('Sivanathan', 'Kandasamy', '791234567V', 'Father', '0778901234', 'sivanathan.kandasamy@yahoo.com', 'No. 56, Temple Road, Anuradhapura', 'Businessman'),
('Ruwan', 'Rathnayake', '761234567V', 'Father', '0789012345', 'ruwan.rathnayake@gmail.com', 'No. 78, Lake Drive, Anuradhapura', 'Farmer'),
('Tharushi', 'Dissanayake', '861234567V', 'Mother', '0790123456', 'tharushi.dissanayake@hotmail.com', 'No. 91, Temple Street, Anuradhapura', 'Nurse'),
('Kasun', 'Ariyaratne', '741234567V', 'Father', '0701234567', 'kasun.ariyaratne@sltnet.lk', 'No. 19, Main Road, Anuradhapura', 'Soldier');

-- ============================================
-- SAMPLE STUDENTS (Sri Lankan)
-- ============================================

INSERT IGNORE INTO student (student_id, first_name, last_name, dob, gender, nic, email, phone, address, grade_id, guardian_id, registration_date, is_active) VALUES 
('STU-0001', 'Dilshan', 'Bandara', '2012-05-15', 'Male', '201245678908', 'dilshan.bandara@gmail.com', '0719876543', 'No. 45, Main Street, Anuradhapura', 7, 1, '2024-01-15', TRUE),
('STU-0002', 'Sanduni', 'Silva', '2012-08-22', 'Female', '201234567897', 'sanduni.silva@yahoo.com', '0728765432', 'No. 23, School Road, Anuradhapura', 7, 2, '2024-01-16', TRUE),
('STU-0003', 'Isuru', 'Perera', '2011-03-10', 'Male', '201123456789', 'isuru.perera@hotmail.com', '0737654321', 'No. 67, New Town, Anuradhapura', 8, 3, '2024-01-17', TRUE),
('STU-0004', 'Ayesha', 'Mohamed', '2012-11-05', 'Female', '201298765432', 'ayesha.mohamed@gmail.com', '0746543210', 'No. 12, Mosque Road, Anuradhapura', 7, 4, '2024-01-18', TRUE),
('STU-0005', 'Nipun', 'Fernando', '2011-07-18', 'Male', '201187654321', 'nipun.fernando@sltnet.lk', '0755432109', 'No. 89, Hospital Road, Anuradhapura', 8, 5, '2024-01-19', TRUE),
('STU-0006', 'Rashmi', 'Jayasinghe', '2012-02-28', 'Female', '201276543210', 'rashmi.jayasinghe@gmail.com', '0764321098', 'No. 34, College Avenue, Anuradhapura', 7, 6, '2024-01-20', TRUE),
('STU-0007', 'Vithusan', 'Kandasamy', '2011-09-12', 'Male', '201165432109', 'vithusan.kandasamy@yahoo.com', '0773210987', 'No. 56, Temple Road, Anuradhapura', 8, 7, '2024-01-21', TRUE),
('STU-0008', 'Thisara', 'Rathnayake', '2012-04-30', 'Male', '201254321098', 'thisara.rathnayake@gmail.com', '0782109876', 'No. 78, Lake Drive, Anuradhapura', 7, 8, '2024-01-22', TRUE),
('STU-0009', 'Senuja', 'Dissanayake', '2011-12-08', 'Female', '201143210987', 'senuja.dissanayake@hotmail.com', '0791098765', 'No. 91, Temple Street, Anuradhapura', 8, 9, '2024-01-23', TRUE),
('STU-0010', 'Yasith', 'Ariyaratne', '2012-06-25', 'Male', '201232109876', 'yasith.ariyaratne@sltnet.lk', '0700987654', 'No. 19, Main Road, Anuradhapura', 7, 10, '2024-01-24', TRUE);

-- Create user accounts for students (password: student123)
INSERT IGNORE INTO user (username, password, email, user_type_id) VALUES 
('dilshan.bandara', 'admin123', 'dilshan.bandara@gmail.com', 3),
('sanduni.silva', 'admin123', 'sanduni.silva@yahoo.com', 3),
('isuru.perera', 'admin123', 'isuru.perera@hotmail.com', 3),
('ayesha.mohamed', 'admin123', 'ayesha.mohamed@gmail.com', 3),
('nipun.fernando', 'admin123', 'nipun.fernando@sltnet.lk', 3),
('rashmi.jayasinghe', 'admin123', 'rashmi.jayasinghe@gmail.com', 3),
('vithusan.kandasamy', 'admin123', 'vithusan.kandasamy@yahoo.com', 3),
('thisara.rathnayake', 'admin123', 'thisara.rathnayake@gmail.com', 3),
('senuja.dissanayake', 'admin123', 'senuja.dissanayake@hotmail.com', 3),
('yasith.ariyaratne', 'admin123', 'yasith.ariyaratne@sltnet.lk', 3);

-- Update student records with user IDs
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'dilshan.bandara') WHERE student_id = 'STU-0001';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'sanduni.silva') WHERE student_id = 'STU-0002';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'isuru.perera') WHERE student_id = 'STU-0003';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'ayesha.mohamed') WHERE student_id = 'STU-0004';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'nipun.fernando') WHERE student_id = 'STU-0005';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'rashmi.jayasinghe') WHERE student_id = 'STU-0006';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'vithusan.kandasamy') WHERE student_id = 'STU-0007';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'thisara.rathnayake') WHERE student_id = 'STU-0008';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'senuja.dissanayake') WHERE student_id = 'STU-0009';
UPDATE student SET user_id = (SELECT user_id FROM user WHERE username = 'yasith.ariyaratne') WHERE student_id = 'STU-0010';

-- ============================================
-- SAMPLE TEACHERS (Sri Lankan)
-- ============================================

INSERT IGNORE INTO teacher (NIC, first_name, last_name, dob, gender, email, phone, address, teacher_type_id, username, password, salary, hire_date, status_id) VALUES 
('851234567V', 'Sunil', 'Wijesinghe', '1985-03-15', 'Male', 'sunil.wijesinghe@educore.lk', '0711111111', 'No. 10, Teachers Quarters, Anuradhapura', 1, 'sunil.wijesinghe', 'admin123', 75000.00, '2020-01-15', 1),
('821234567V', 'Malini', 'Samarasinghe', '1987-07-22', 'Female', 'malini.samarasinghe@educore.lk', '0722222222', 'No. 11, Teachers Quarters, Anuradhapura', 1, 'malini.samarasinghe', 'admin123', 72000.00, '2019-06-10', 1),
('791234567V', 'Kamal', 'Senaratne', '1984-11-08', 'Male', 'kamal.senaratne@educore.lk', '0733333333', 'No. 12, Teachers Quarters, Anuradhapura', 1, 'kamal.senaratne', 'admin123', 78000.00, '2018-03-20', 1),
('861234567V', 'Deepika', 'Ranaweera', '1988-05-30', 'Female', 'deepika.ranaweera@educore.lk', '0744444444', 'No. 13, Teachers Quarters, Anuradhapura', 1, 'deepika.ranaweera', 'admin123', 71000.00, '2021-01-05', 1),
('801234567V', 'Pradeep', 'Jayathilake', '1986-09-12', 'Male', 'pradeep.jayathilake@educore.lk', '0755555555', 'No. 14, Teachers Quarters, Anuradhapura', 1, 'pradeep.jayathilake', 'admin123', 76000.00, '2019-09-15', 1),
('841234567V', 'Samanthi', 'Perera', '1989-02-18', 'Female', 'samanthi.perera@educore.lk', '0766666666', 'No. 15, Teachers Quarters, Anuradhapura', 1, 'samanthi.perera', 'admin123', 73000.00, '2020-07-01', 1),
('771234567V', 'Nimal', 'Hettiarachchi', '1983-12-25', 'Male', 'nimal.hettiarachchi@educore.lk', '0777777777', 'No. 16, Teachers Quarters, Anuradhapura', 1, 'nimal.hettiarachchi', 'admin123', 80000.00, '2017-04-10', 1),
('831234567V', 'Rasika', 'Karunaratne', '1985-08-05', 'Male', 'rasika.karunaratne@educore.lk', '0788888888', 'No. 17, Teachers Quarters, Anuradhapura', 1, 'rasika.karunaratne', 'admin123', 74000.00, '2019-02-28', 1),
('871234567V', 'Nilushi', 'Silva', '1990-04-14', 'Female', 'nilushi.silva@educore.lk', '0799999999', 'No. 18, Teachers Quarters, Anuradhapura', 1, 'nilushi.silva', 'admin123', 70000.00, '2022-01-10', 1),
('761234567V', 'Asela', 'Bandara', '1982-10-20', 'Male', 'asela.bandara@educore.lk', '0700000000', 'No. 19, Teachers Quarters, Anuradhapura', 1, 'asela.bandara', 'admin123', 82000.00, '2016-06-15', 1);

-- Create user accounts for teachers (password: teacher123)
INSERT IGNORE INTO user (username, password, email, user_type_id) VALUES 
('sunil.wijesinghe', 'admin123', 'sunil.wijesinghe@educore.lk', 2),
('malini.samarasinghe', 'admin123', 'malini.samarasinghe@educore.lk', 2),
('kamal.senaratne', 'admin123', 'kamal.senaratne@educore.lk', 2),
('deepika.ranaweera', 'admin123', 'deepika.ranaweera@educore.lk', 2),
('pradeep.jayathilake', 'admin123', 'pradeep.jayathilake@educore.lk', 2),
('samanthi.perera', 'admin123', 'samanthi.perera@educore.lk', 2),
('nimal.hettiarachchi', 'admin123', 'nimal.hettiarachchi@educore.lk', 2),
('rasika.karunaratne', 'admin123', 'rasika.karunaratne@educore.lk', 2),
('nilushi.silva', 'admin123', 'nilushi.silva@educore.lk', 2),
('asela.bandara', 'admin123', 'asela.bandara@educore.lk', 2);

-- Update teacher records with user IDs
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'sunil.wijesinghe') WHERE NIC = '851234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'malini.samarasinghe') WHERE NIC = '821234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'kamal.senaratne') WHERE NIC = '791234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'deepika.ranaweera') WHERE NIC = '861234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'pradeep.jayathilake') WHERE NIC = '801234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'samanthi.perera') WHERE NIC = '841234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'nimal.hettiarachchi') WHERE NIC = '771234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'rasika.karunaratne') WHERE NIC = '831234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'nilushi.silva') WHERE NIC = '871234567V';
UPDATE teacher SET user_id = (SELECT user_id FROM user WHERE username = 'asela.bandara') WHERE NIC = '761234567V';

-- ============================================
-- SAMPLE SUBJECTS
-- ============================================

INSERT IGNORE INTO subject (subject_name, grade_id, description) VALUES 
('Mathematics', 7, 'Mathematics for Grade 7'),
('Science', 7, 'Science for Grade 7'),
('English', 7, 'English Language for Grade 7'),
('Sinhala', 7, 'Sinhala Language for Grade 7'),
('Mathematics', 8, 'Mathematics for Grade 8'),
('Science', 8, 'Science for Grade 8'),
('English', 8, 'English Language for Grade 8'),
('Sinhala', 8, 'Sinhala Language for Grade 8'),
('Mathematics', 9, 'Mathematics for Grade 9'),
('Science', 9, 'Science for Grade 9'),
('English', 9, 'English Language for Grade 9'),
('Sinhala', 9, 'Sinhala Language for Grade 9');

-- ============================================
-- SAMPLE TEACHER-SUBJECT ASSIGNMENTS
-- ============================================

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '851234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '851234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '821234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'Science' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '821234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'Science' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '791234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'English' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '791234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'English' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '861234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '861234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '801234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '801234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Science' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '841234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'English' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '841234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '771234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '771234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '831234567V', subject_id, 7, 2024 FROM subject WHERE subject_name = 'English' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '831234567V', subject_id, 8, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '871234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '871234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'English' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '761234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Science' AND grade_id = 9 LIMIT 1;

INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) 
SELECT '761234567V', subject_id, 9, 2024 FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 9 LIMIT 1;

-- ============================================
-- SAMPLE TEACHER-CLASS ASSIGNMENTS
-- ============================================

INSERT IGNORE INTO teacher_class (teacher_NIC, grade_id, is_class_teacher, academic_year) VALUES 
('851234567V', 7, TRUE, 2024),
('821234567V', 8, TRUE, 2024),
('791234567V', 9, TRUE, 2024),
('861234567V', 7, FALSE, 2024),
('801234567V', 8, FALSE, 2024),
('841234567V', 9, FALSE, 2024),
('771234567V', 7, FALSE, 2024),
('831234567V', 8, FALSE, 2024),
('871234567V', 9, FALSE, 2024),
('761234567V', 7, FALSE, 2024);

-- ============================================
-- SAMPLE EXAMS
-- ============================================

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'Mathematics 1st Term', 1, subject_id, 7, '2024-03-15', 100, 120, 'First term mathematics examination' 
FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'Science 1st Term', 1, subject_id, 7, '2024-03-18', 100, 90, 'First term science examination' 
FROM subject WHERE subject_name = 'Science' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'English 1st Term', 1, subject_id, 7, '2024-03-20', 100, 90, 'First term English examination' 
FROM subject WHERE subject_name = 'English' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'Sinhala 1st Term', 1, subject_id, 7, '2024-03-22', 100, 90, 'First term Sinhala examination' 
FROM subject WHERE subject_name = 'Sinhala' AND grade_id = 7 LIMIT 1;

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'Mathematics 1st Term', 1, subject_id, 8, '2024-03-16', 100, 120, 'First term mathematics examination' 
FROM subject WHERE subject_name = 'Mathematics' AND grade_id = 8 LIMIT 1;

INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) 
SELECT 'Science 1st Term', 1, subject_id, 8, '2024-03-19', 100, 90, 'First term science examination' 
FROM subject WHERE subject_name = 'Science' AND grade_id = 8 LIMIT 1;

-- ============================================
-- SAMPLE FEES
-- ============================================

INSERT IGNORE INTO fee (student_id, fee_type_id, amount, due_date, status) VALUES 
('STU-0001', 1, 1000.00, '2024-02-01', 'Pending'),
('STU-0001', 2, 500.00, '2024-02-01', 'Pending'),
('STU-0002', 1, 1000.00, '2024-02-01', 'Pending'),
('STU-0002', 2, 500.00, '2024-02-01', 'Pending'),
('STU-0003', 1, 1000.00, '2024-02-01', 'Pending'),
('STU-0003', 2, 500.00, '2024-02-01', 'Pending'),
('STU-0004', 1, 1000.00, '2024-02-01', 'Pending'),
('STU-0004', 2, 500.00, '2024-02-01', 'Pending'),
('STU-0005', 1, 1000.00, '2024-02-01', 'Pending'),
('STU-0005', 2, 500.00, '2024-02-01', 'Pending');

-- Insert sample teacher
INSERT IGNORE INTO teacher (NIC, first_name, last_name, dob, gender, email, phone, address, teacher_type_id, username, password, salary, hire_date, status_id, user_id) VALUES 
('801234567V', 'Dr. Sarah', 'Smith', '1985-03-20', 'Female', 'sarah.smith@educore.com', '0771234569', '456 School Road, Anuradhapura', 1, 'teacher1', '$2a$12$7aWoXIZSUOvCAdHXC1.7MuVF8eZLeLsXrBoYhaKFPvtxZaAaTkLxO', 75000.00, '2020-01-15', 1, 2);

-- Insert sample subject
INSERT IGNORE INTO subject (subject_name, grade_id, description) VALUES 
('Mathematics', 1, 'Mathematics for Grade 6'),
('Science', 1, 'Science for Grade 6'),
('English', 1, 'English Language for Grade 6');

-- Assign sample teacher to Grade 6A (class teacher) and Grade 6B
INSERT IGNORE INTO teacher_class (teacher_NIC, grade_id, is_class_teacher, academic_year) VALUES 
('801234567V', 1, TRUE, YEAR(CURDATE())),
('801234567V', 2, FALSE, YEAR(CURDATE()));

-- Assign sample teacher to teach Mathematics and Science in Grade 6
INSERT IGNORE INTO teacher_subject (teacher_NIC, subject_id, grade_id, academic_year) VALUES 
('801234567V', 1, 1, YEAR(CURDATE())),
('801234567V', 2, 1, YEAR(CURDATE()));

-- Insert sample exam
INSERT IGNORE INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) VALUES 
('Term Test 1 - Mathematics', 1, 1, 1, CURDATE() + INTERVAL 7 DAY, 100.00, 90, 'First term test for Mathematics');

-- Insert sample fee (using SDS Contribution - fee_type_id = 1)
INSERT IGNORE INTO fee (student_id, fee_type_id, amount, due_date, status) VALUES 
('STU-0001', 1, 1000.00, CURDATE() + INTERVAL 30 DAY, 'Pending');

-- Insert sample books
INSERT IGNORE INTO book (title, author, isbn, category_id, quantity, available_qty, publisher, publication_year, price, location, barcode) VALUES 
('Introduction to Java Programming', 'Daniel Liang', '978-0134694024', 3, 10, 8, 'Pearson', 2017, 1500.00, 'A-12', 'BK001'),
('Data Structures and Algorithms', 'Mark Allen Weiss', '978-0132846373', 3, 8, 6, 'Pearson', 2016, 1800.00, 'A-13', 'BK002'),
('Advanced Mathematics', 'James Stewart', '978-1305271780', 2, 12, 10, 'Cengage Learning', 2015, 2200.00, 'B-01', 'BK003'),
('Physics for Scientists', 'Raymond Serway', '978-1305112448', 2, 15, 12, 'Cengage Learning', 2018, 2500.00, 'B-02', 'BK004'),
('World History', 'William Duiker', '978-1305089108', 5, 6, 5, 'Cengage Learning', 2015, 1200.00, 'C-01', 'BK005'),
('Steve Jobs Biography', 'Walter Isaacson', '978-1451648539', 1, 5, 4, 'Simon & Schuster', 2011, 800.00, 'C-02', 'BK006'),
('Clean Code', 'Robert Martin', '978-0132350884', 6, 8, 7, 'Prentice Hall', 2008, 1700.00, 'A-14', 'BK007'),
('The Pragmatic Programmer', 'Andrew Hunt', '978-0201616224', 6, 7, 6, 'Addison-Wesley', 1999, 1600.00, 'A-15', 'BK008');

-- ============================================
-- END OF DATABASE SCHEMA
-- ============================================