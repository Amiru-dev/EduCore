-- School Management System Database
-- EduCore School Management System
-- MySQL Database Schema (3NF)

-- Create Database
CREATE DATABASE IF NOT EXISTS school_management_db;
USE school_management_db;

-- ============================================
-- USER MANAGEMENT TABLES
-- ============================================

-- User Type Lookup Table
CREATE TABLE user_type (
    user_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default user types
INSERT INTO user_type (type_name, description) VALUES 
('Admin', 'System Administrator with full access'),
('Teacher', 'Teacher with limited access'),
('Student', 'Student with view-only access');

-- User Authentication Table
CREATE TABLE user (
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
CREATE TABLE grade (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    grade_name VARCHAR(50) NOT NULL UNIQUE,
    section VARCHAR(10),
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default grades
INSERT INTO grade (grade_name, section) VALUES 
('Grade 6', 'A'), ('Grade 6', 'B'), ('Grade 6', 'C'),
('Grade 7', 'A'), ('Grade 7', 'B'), ('Grade 7', 'C'),
('Grade 8', 'A'), ('Grade 8', 'B'), ('Grade 8', 'C'),
('Grade 9', 'A'), ('Grade 9', 'B'), ('Grade 9', 'C'),
('Grade 10', 'A'), ('Grade 10', 'B'), ('Grade 10', 'C'),
('Grade 11', 'A'), ('Grade 11', 'B'), ('Grade 11', 'C'),
('Grade 12', 'A'), ('Grade 12', 'B'), ('Grade 12', 'C'),
('Grade 13', 'A'), ('Grade 13', 'B'), ('Grade 13', 'C');

-- Guardian/Parent Table
CREATE TABLE guardian (
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
CREATE TABLE student (
    student_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
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
CREATE TABLE student_attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    date_time DATETIME NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, DATE(date_time))
);

-- ============================================
-- TEACHER/STAFF MANAGEMENT TABLES
-- ============================================

-- Teacher Type Lookup Table
CREATE TABLE teacher_type (
    teacher_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default teacher types
INSERT INTO teacher_type (type_name, description) VALUES 
('Teacher', 'Regular Teacher'),
('Principal', 'School Principal'),
('Office Staff', 'Administrative Staff'),
('Support Staff', 'Support Staff');

-- Teacher Status Lookup Table
CREATE TABLE teacher_status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default teacher statuses
INSERT INTO teacher_status (status_name, description) VALUES 
('Active', 'Currently employed'),
('Inactive', 'Not currently employed'),
('On Leave', 'On leave of absence'),
('Retired', 'Retired from service');

-- Teacher/Staff Table
CREATE TABLE teacher (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    NIC VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
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
CREATE TABLE teacher_attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_NIC VARCHAR(20) NOT NULL,
    date_time DATETIME NOT NULL,
    check_in TIME NOT NULL,
    check_out TIME,
    duration INT COMMENT 'Duration in minutes',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (teacher_NIC) REFERENCES teacher(NIC) ON DELETE CASCADE,
    UNIQUE KEY unique_teacher_attendance (teacher_NIC, DATE(date_time))
);

-- Leave Type Lookup Table
CREATE TABLE leave_type (
    leave_type_id INT PRIMARY KEY AUTO_INCREMENT,
    leave_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    max_days INT DEFAULT 30,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default leave types
INSERT INTO leave_type (leave_type_name, description, max_days) VALUES 
('Sick Leave', 'Medical leave', 14),
('Casual Leave', 'Personal leave', 7),
('Annual Leave', 'Yearly vacation', 30),
('Maternity Leave', 'Maternity leave', 84),
('Paternity Leave', 'Paternity leave', 7);

-- Teacher Leave Table
CREATE TABLE teacher_leave (
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
CREATE TABLE subject (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL UNIQUE,
    grade_id INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (grade_id) REFERENCES grade(grade_id) ON DELETE CASCADE
);

-- Exam Type Lookup Table
CREATE TABLE exam_type (
    exam_type_id INT PRIMARY KEY AUTO_INCREMENT,
    exam_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default exam types
INSERT INTO exam_type (exam_type_name, description) VALUES 
('Term Test', 'Monthly term examination'),
('Mid Term', 'Mid-year examination'),
('Final Exam', 'Final year examination'),
('Practical', 'Practical examination');

-- Exam Table
CREATE TABLE exam (
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
CREATE TABLE result (
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
CREATE TABLE fee_type (
    fee_type_id INT PRIMARY KEY AUTO_INCREMENT,
    fee_type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    default_amount DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default fee types
INSERT INTO fee_type (fee_type_name, description, default_amount) VALUES 
('Tuition Fee', 'Monthly tuition fee', 5000.00),
('Lab Fee', 'Laboratory fee', 1000.00),
('Library Fee', 'Library membership fee', 500.00),
('Transport Fee', 'Transportation fee', 2000.00),
('Sports Fee', 'Sports and activities fee', 750.00),
('Exam Fee', 'Examination fee', 1500.00);

-- Payment Method Lookup Table
CREATE TABLE payment_method (
    payment_method_id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default payment methods
INSERT INTO payment_method (method_name, description) VALUES 
('Cash', 'Cash payment'),
('Card', 'Credit/Debit card'),
('Bank Transfer', 'Bank transfer'),
('Check', 'Check payment');

-- Fee Table
CREATE TABLE fee (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    fee_type_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    due_date DATE NOT NULL,
    paid_date DATE,
    payment_method_id INT,
    status ENUM('Pending', 'Paid', 'Overdue', 'Cancelled') DEFAULT 'Pending',
    receipt_no VARCHAR(50) UNIQUE,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (fee_type_id) REFERENCES fee_type(fee_type_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id) ON DELETE SET NULL
);

-- ============================================
-- TRANSPORT TABLES
-- ============================================

-- Vehicle Type Lookup Table
CREATE TABLE vehicle_type (
    vehicle_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default vehicle types
INSERT INTO vehicle_type (type_name, description) VALUES 
('Bus', 'School bus'),
('Van', 'School van'),
('Mini Bus', 'Mini school bus');

-- Vehicle Status Lookup Table
CREATE TABLE vehicle_status (
    vehicle_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default vehicle statuses
INSERT INTO vehicle_status (status_name, description) VALUES 
('Active', 'In service'),
('Maintenance', 'Under maintenance'),
('Retired', 'Retired from service'),
('Out of Service', 'Temporarily out of service');

-- Vehicle Table
CREATE TABLE vehicle (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    plate_no VARCHAR(20) NOT NULL UNIQUE,
    vehicle_type_id INT NOT NULL,
    capacity INT NOT NULL,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(20) NOT NULL,
    vehicle_status_id INT NOT NULL,
    make VARCHAR(50),
    model VARCHAR(50),
    year INT,
    fuel_type VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_type_id) REFERENCES vehicle_type(vehicle_type_id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_status_id) REFERENCES vehicle_status(vehicle_status_id) ON DELETE CASCADE
);

-- Transport Route Table
CREATE TABLE transport_route (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    route_name VARCHAR(100) NOT NULL UNIQUE,
    start_point VARCHAR(255) NOT NULL,
    end_point VARCHAR(255) NOT NULL,
    distance_km DECIMAL(5, 2),
    vehicle_id INT NOT NULL,
    monthly_fee DECIMAL(10, 2) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
);

-- Student Transport Assignment Table
CREATE TABLE student_transport (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    route_id INT NOT NULL,
    pickup_point VARCHAR(255) NOT NULL,
    drop_point VARCHAR(255),
    assigned_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES transport_route(route_id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_transport (student_id, route_id)
);

-- ============================================
-- EVENTS & NOTICES TABLES
-- ============================================

-- Event Status Lookup Table
CREATE TABLE event_status (
    event_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default event statuses
INSERT INTO event_status (status_name, description) VALUES 
('Active', 'Upcoming event'),
('Completed', 'Event completed'),
('Cancelled', 'Event cancelled'),
('Postponed', 'Event postponed');

-- Event Table
CREATE TABLE event (
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
CREATE TABLE transaction_type (
    transaction_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default transaction types
INSERT INTO transaction_type (type_name, description) VALUES 
('Income', 'Income transaction'),
('Expense', 'Expense transaction');

-- Finance Table
CREATE TABLE finance (
    finance_id INT PRIMARY KEY AUTO_INCREMENT,
    description TEXT NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    transaction_type_id INT NOT NULL,
    payment_method_id INT,
    transaction_date DATE NOT NULL,
    category VARCHAR(100),
    added_by VARCHAR(100),
    reference_no VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_type_id) REFERENCES transaction_type(transaction_type_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id) ON DELETE SET NULL
);

-- ============================================
-- LIBRARY & INVENTORY TABLES
-- ============================================

-- Book Category Lookup Table
CREATE TABLE book_category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default book categories
INSERT INTO book_category (category_name, description) VALUES 
('Fiction', 'Fiction books'),
('Science', 'Science books'),
('Textbook', 'Textbooks'),
('Reference', 'Reference books'),
('History', 'History books'),
('Biography', 'Biographies'),
('Technology', 'Technology books');

-- Book Table
CREATE TABLE book (
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
CREATE TABLE book_issue (
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
CREATE INDEX idx_teacher_active ON teacher(status_id);

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
CREATE INDEX idx_fee_status ON fee(status_id);
CREATE INDEX idx_fee_date ON fee(due_date);

-- Attendance indexes
CREATE INDEX idx_student_attendance_student ON student_attendance(student_id);
CREATE INDEX idx_student_attendance_date ON student_attendance(date_time);
CREATE INDEX idx_teacher_attendance_teacher ON teacher_attendance(teacher_NIC);
CREATE INDEX idx_teacher_attendance_date ON teacher_attendance(date_time);

-- ============================================
-- SAMPLE DATA FOR TESTING
-- ============================================

-- Insert sample admin user
INSERT INTO user (username, password, email, user_type_id) VALUES 
('admin', 'admin123', 'admin@educore.com', 1);

-- Insert sample teacher user
INSERT INTO user (username, password, email, user_type_id) VALUES 
('teacher1', 'teacher123', 'teacher1@educore.com', 2);

-- Insert sample student user
INSERT INTO user (username, password, email, user_type_id) VALUES 
('student1', 'student123', 'student1@educore.com', 3);

-- Insert sample guardian
INSERT INTO guardian (first_name, last_name, nic, relationship, phone, email, address, occupation) VALUES 
('John', 'Doe', '901234567V', 'Father', '+94771234567', 'john.doe@email.com', '123 Main Street, Anuradhapura', 'Engineer');

-- Insert sample student
INSERT INTO student (student_id, first_name, last_name, dob, gender, nic, email, phone, address, grade_id, guardian_id, user_id, registration_date) VALUES 
('STU-0001', 'Jane', 'Doe', '2008-05-15', 'Female', '081234567V', 'jane.doe@email.com', '+94771234568', '123 Main Street, Anuradhapura', 1, 1, 3, CURDATE());

-- Insert sample teacher
INSERT INTO teacher (NIC, first_name, last_name, dob, gender, email, phone, address, teacher_type_id, username, password, salary, hire_date, status_id, user_id) VALUES 
('801234567V', 'Dr. Sarah', 'Smith', '1985-03-20', 'Female', 'sarah.smith@educore.com', '+94771234569', '456 School Road, Anuradhapura', 1, 'teacher1', 'teacher123', 75000.00, '2020-01-15', 1, 2);

-- Insert sample subject
INSERT INTO subject (subject_name, grade_id, description) VALUES 
('Mathematics', 1, 'Mathematics for Grade 6'),
('Science', 1, 'Science for Grade 6'),
('English', 1, 'English Language for Grade 6');

-- Insert sample exam
INSERT INTO exam (exam_name, exam_type_id, subject_id, grade_id, exam_date, max_marks, duration_minutes, description) VALUES 
('Term Test 1 - Mathematics', 1, 1, 1, CURDATE() + INTERVAL 7 DAY, 100.00, 90, 'First term test for Mathematics');

-- Insert sample fee
INSERT INTO fee (student_id, fee_type_id, amount, due_date, status) VALUES 
('STU-0001', 1, 5000.00, CURDATE() + INTERVAL 30 DAY, 'Pending');

-- ============================================
-- END OF DATABASE SCHEMA
-- ============================================