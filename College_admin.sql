-- =============================================================================
-- NEW “college_admin_new” DATABASE: FULL CREATION & INSERTION SCRIPT
-- =============================================================================

-- 1) Create database and switch to it
CREATE DATABASE IF NOT EXISTS college_admin_new;
USE college_admin_new;

-- 2) Create all tables (VARCHAR(10) PK style + associative tables)
--------------------------------------------------------------------------------
CREATE TABLE Department (
    DepartmentID    VARCHAR(10)   PRIMARY KEY,  
    Department_name VARCHAR(100)  NOT NULL,
    HOD_Name        VARCHAR(100)  NOT NULL
);

CREATE TABLE Student (
    StudentID  VARCHAR(10)  PRIMARY KEY,
    Name       VARCHAR(100) NOT NULL,
    Address    VARCHAR(255) NOT NULL,
    Phone_no   VARCHAR(20)  NOT NULL,
    Course     VARCHAR(100) NOT NULL,
    Password   VARCHAR(100) NOT NULL
);

CREATE TABLE Faculty (
    FacultyID VARCHAR(10)   PRIMARY KEY,
    Name      VARCHAR(100)  NOT NULL,
    Address   VARCHAR(255)  NOT NULL,
    Phone_no  VARCHAR(20)   NOT NULL,
    Course    VARCHAR(100)  NOT NULL,
    Password  VARCHAR(100)  NOT NULL
);

CREATE TABLE Event (
    EventID       VARCHAR(10)   PRIMARY KEY,
    Event_name    VARCHAR(100)  NOT NULL,
    Description   TEXT,
    Date_of_event DATE          NOT NULL,
    Venue         VARCHAR(100)  NOT NULL
);

CREATE TABLE Result (
    ResultID       VARCHAR(10)   PRIMARY KEY,
    Result_details VARCHAR(255)  NOT NULL
);

CREATE TABLE Library (
    LibraryID      VARCHAR(10)   PRIMARY KEY,
    Name           VARCHAR(100)  NOT NULL,
    Location       VARCHAR(100)  NOT NULL,
    Available_Books INT          NOT NULL,
    Total_Books     INT          NOT NULL
);

CREATE TABLE Course (
    CourseID     VARCHAR(10)   PRIMARY KEY,
    Name         VARCHAR(100)  NOT NULL,
    DepartmentID VARCHAR(10),
    Semester     INT           NOT NULL,
    FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID)
);

CREATE TABLE Course_Subjects (
    CourseID     VARCHAR(10),
    Subject_Name VARCHAR(100)  NOT NULL,
    PRIMARY KEY (CourseID, Subject_Name),
    FOREIGN KEY (CourseID) REFERENCES Course(CourseID)
);

CREATE TABLE Finance (
    FinanceID VARCHAR(10)     PRIMARY KEY,
    Category  VARCHAR(100)    NOT NULL,
    Revenue   DECIMAL(15,2)   NOT NULL,
    Expenses  DECIMAL(15,2)   NOT NULL,
    Budget    DECIMAL(15,2)   NOT NULL,
    Date      DATE            NOT NULL
);

CREATE TABLE Hostel (
    HostelID        VARCHAR(10)  PRIMARY KEY,
    Rooms           INT          NOT NULL,
    Capacity        INT          NOT NULL,
    Block           VARCHAR(100) NOT NULL,
    Available_Space INT          NOT NULL
);

CREATE TABLE PlacementCompany (
    CompanyID           VARCHAR(10)   PRIMARY KEY,
    Name                VARCHAR(100)  NOT NULL,
    Industry            VARCHAR(100),
    Job_Roles_Offered   VARCHAR(100)  NOT NULL,
    Package_Range       VARCHAR(100),
    Eligibility_Criteria VARCHAR(100),
    Visit_Date          DATE          NOT NULL,
    Contact_Person      VARCHAR(100),
    Contact_Email       VARCHAR(100)  UNIQUE
);

CREATE TABLE Transport (
    TransportID    VARCHAR(10)   PRIMARY KEY,
    Route          VARCHAR(100)  NOT NULL,
    Driver_Name    VARCHAR(100)  NOT NULL,
    Vehicle_Number VARCHAR(100)  UNIQUE NOT NULL,
    Capacity       INT           NOT NULL
);

CREATE TABLE Classroom (
    ClassroomID        VARCHAR(10)   PRIMARY KEY,
    Room_No            VARCHAR(100)  UNIQUE NOT NULL,
    Floor_No           INT           NOT NULL,
    Type               VARCHAR(100)  NOT NULL,
    Capacity           INT           NOT NULL,
    Availability_Status ENUM('Available','Occupied') NOT NULL
);

CREATE TABLE Book (
    BookID             VARCHAR(10)   PRIMARY KEY,
    Title              VARCHAR(100)  NOT NULL,
    Author             VARCHAR(100)  NOT NULL,
    Publisher          VARCHAR(100)  NOT NULL,
    Availability_Status ENUM('Available','Issued') NOT NULL,
    Issue_Date         DATE,
    Return_Date        DATE,
    LibraryID          VARCHAR(10),
    FOREIGN KEY (LibraryID) REFERENCES Library(LibraryID)
);

CREATE TABLE Inside (
    StudentID    VARCHAR(10),
    DepartmentID VARCHAR(10),
    PRIMARY KEY (StudentID, DepartmentID),
    FOREIGN KEY (StudentID)    REFERENCES Student(StudentID)    ON DELETE CASCADE,
    FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID) ON DELETE CASCADE
);

CREATE TABLE Enrolls (
    FacultyID    VARCHAR(10),
    DepartmentID VARCHAR(10),
    PRIMARY KEY (FacultyID, DepartmentID),
    FOREIGN KEY (FacultyID)    REFERENCES Faculty(FacultyID)     ON DELETE CASCADE,
    FOREIGN KEY (DepartmentID) REFERENCES Department(DepartmentID) ON DELETE CASCADE
);

CREATE TABLE Handles (
    EventID   VARCHAR(10),
    FacultyID VARCHAR(10),
    PRIMARY KEY (EventID, FacultyID),
    FOREIGN KEY (EventID)   REFERENCES Event(EventID)           ON DELETE CASCADE,
    FOREIGN KEY (FacultyID) REFERENCES Faculty(FacultyID)       ON DELETE CASCADE
);

CREATE TABLE Teaches (
    StudentID VARCHAR(10),
    FacultyID VARCHAR(10),
    PRIMARY KEY (StudentID, FacultyID),
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID)       ON DELETE CASCADE,
    FOREIGN KEY (FacultyID) REFERENCES Faculty(FacultyID)       ON DELETE CASCADE
);

CREATE TABLE Participates (
    EventID   VARCHAR(10),
    StudentID VARCHAR(10),
    PRIMARY KEY (EventID, StudentID),
    FOREIGN KEY (EventID)   REFERENCES Event(EventID)           ON DELETE CASCADE,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID)       ON DELETE CASCADE
);

CREATE TABLE View_Result (
    ResultID  VARCHAR(10),
    StudentID VARCHAR(10),
    PRIMARY KEY (StudentID, ResultID),
    FOREIGN KEY (ResultID)  REFERENCES Result(ResultID)        ON DELETE CASCADE,
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID)       ON DELETE CASCADE
);

--------------------------------------------------------------------------------
-- 3) INSERTION OF DATA (all keys in VARCHAR(10) format)

-- Departments
INSERT INTO Department VALUES
('D001','Computer Science','Dr. Alan Turing'),
('D002','Electrical Engineering','Dr. Nikola Tesla'),
('D003','Mechanical Engineering','Dr. Isaac Newton'),
('D004','Civil Engineering','Dr. Thomas Edison'),
('D005','Physics','Dr. Albert Einstein'),
('D006','Mathematics','Dr. Katherine Johnson'),
('D007','Chemistry','Dr. Marie Curie'),
('D008','Biology','Dr. Charles Darwin'),
('D009','Business Administration','Dr. Peter Drucker'),
('D010','Economics','Dr. Adam Smith');

-- Students
INSERT INTO Student VALUES
('S001','John Doe','123 Main St, Cityville','+1234567890','Computer Science','password123'),
('S002','Jane Smith','456 Oak Ave, Townsburg','+1234567891','Electrical Engineering','password456'),
('S003','Alex Johnson','789 Pine Rd, Villageton','+1234567892','Mechanical Engineering','password789'),
('S004','Sarah Williams','101 Elm Blvd, Hamletsville','+1234567893','Civil Engineering','password101'),
('S005','Michael Brown','202 Maple Dr, Boroughburg','+1234567894','Physics','password202'),
('S006','Emily Davis','303 Birch Ln, Districtville','+1234567895','Mathematics','password303'),
('S007','David Miller','404 Cedar St, Regiontown','+1234567896','Chemistry','password404'),
('S008','Lisa Wilson','505 Pine Ave, Provinceland','+1234567897','Biology','password505'),
('S009','Robert Taylor','606 Oak Rd, Territoryville','+1234567898','Business Administration','password606'),
('S010','Jennifer Moore','707 Elm St, Countryburg','+1234567899','Economics','password707');

-- Faculty
INSERT INTO Faculty VALUES
('F001','Prof. James Johnson','111 University Ave, Academictown','+9876543210','Programming','faculty111'),
('F002','Prof. Mary Williams','222 College St, Scholasticville','+9876543211','Circuit Design','faculty222'),
('F003','Prof. Robert Smith','333 Campus Rd, Educationburg','+9876543212','Thermodynamics','faculty333'),
('F004','Prof. Patricia Brown','444 Learning Ln, Knowledgeville','+9876543213','Structural Analysis','faculty444'),
('F005','Prof. Michael Davis','555 Research Blvd, Sciencetown','+9876543214','Quantum Mechanics','faculty555'),
('F006','Prof. Linda Miller','666 Professor Dr, Mathematica','+9876543215','Calculus','faculty666'),
('F007','Prof. William Wilson','777 Academic St, Chemistryville','+9876543216','Organic Chemistry','faculty777'),
('F008','Prof. Elizabeth Taylor','888 Education Ave, Biologyburg','+9876543217','Genetics','faculty888'),
('F009','Prof. David Moore','999 Faculty Rd, Businesstown','+9876543218','Marketing','faculty999'),
('F010','Prof. Barbara Anderson','000 Intellect Blvd, Econoville','+9876543219','Microeconomics','faculty000');

-- Events
INSERT INTO Event VALUES
('E001','Tech Symposium','Annual technology showcase','2025-05-15','Main Auditorium'),
('E002','Engineering Expo','Exhibition of engineering projects','2025-06-20','Engineering Building'),
('E003','Science Fair','Student science project competition','2025-07-10','Science Center'),
('E004','Math Olympiad','Mathematics competition','2025-08-05','Mathematics Department'),
('E005','Career Day','Networking event with industry professionals','2025-09-12','Student Center'),
('E006','Research Conference','Presentation of research papers','2025-10-25','Conference Hall'),
('E007','Hackathon','24-hour coding competition','2025-11-08','Computer Lab'),
('E008','Alumni Meet','Annual gathering of university alumni','2025-12-15','University Grounds'),
('E009','Cultural Festival','Celebration of diverse cultures','2026-01-20','Cultural Center'),
('E010','Sports Tournament','Inter-department sports competition','2026-02-14','Sports Complex');

-- Results
INSERT INTO Result VALUES
('R001','Semester 1 - Fall 2024'),
('R002','Semester 2 - Spring 2025'),
('R003','Midterm Examination 2024'),
('R004','Final Examination 2024'),
('R005','Semester 3 - Fall 2025'),
('R006','Semester 4 - Spring 2026'),
('R007','Project Assessment 2025'),
('R008','Practical Examination 2025'),
('R009','Entrance Examination 2024'),
('R010','Scholarship Test 2025');

-- Library
INSERT INTO Library VALUES
('L001','Central Library','Ground Floor',5000,7000),
('L002','CSE Library','Block A',1200,1500),
('L003','AIML Library','Block B',1000,1300),
('L004','ENTC Library','Block C',950,1200),
('L005','RNA Library','Block D',850,1000),
('L006','Civil Library','Block E',900,1100),
('L007','Mech Library','Block F',1100,1400);

-- Course
INSERT INTO Course VALUES
('CO001','Data Structures','D001',3),
('CO002','Machine Learning Basics','D002',5),
('CO003','Digital Signal Processing','D003',4),
('CO004','Introduction to Robotics','D004',6),
('CO005','Structural Analysis','D005',5),
('CO006','Thermodynamics','D006',2),
('CO007','Operating Systems','D001',4),
('CO008','Deep Learning','D002',6),
('CO009','Microcontrollers','D003',5),
('CO010','Automation and Control Systems','D004',3);

-- Course_Subjects
INSERT INTO Course_Subjects VALUES
('CO001','Stacks'),('CO001','Queues'),('CO001','Trees'),
('CO002','Regression'),('CO002','Classification'),('CO002','Clustering'),
('CO003','Fourier Transform'),('CO003','Filters'),
('CO004','Kinematics'),('CO004','Dynamics'),('CO004','Control Systems'),
('CO005','Trusses'),('CO005','Beams'),('CO005','Frames'),
('CO006','Laws of Thermodynamics'),('CO006','Entropy'),
('CO007','Processes'),('CO007','Memory Management'),
('CO008','Neural Nets'),('CO008','CNNs'),('CO008','RNNs'),
('CO009','8051'),('CO009','PIC'),('CO009','ARM'),
('CO010','PID Control'),('CO010','PLCs'),('CO010','SCADA Systems');

-- Finance
INSERT INTO Finance VALUES
('FI001','Infrastructure',500000.00,320000.00,700000.00,'2023-01-01'),
('FI002','Salaries',800000.00,750000.00,850000.00,'2023-01-10'),
('FI003','Library',100000.00,85000.00,120000.00,'2023-02-01'),
('FI004','Hostel',300000.00,250000.00,350000.00,'2023-02-15'),
('FI005','Events',150000.00,145000.00,160000.00,'2023-03-01'),
('FI006','Maintenance',200000.00,190000.00,220000.00,'2023-03-10'),
('FI007','Transport',180000.00,170000.00,200000.00,'2023-04-01'),
('FI008','Lab Equipment',250000.00,240000.00,270000.00,'2023-04-15'),
('FI009','Scholarships',350000.00,300000.00,400000.00,'2023-05-01'),
('FI010','Miscellaneous',50000.00,40000.00,60000.00,'2023-05-15');

-- Hostel
INSERT INTO Hostel VALUES
('H001',50,200,'A Block',30),
('H002',40,160,'B Block',20),
('H003',60,240,'C Block',50),
('H004',35,140,'D Block',15),
('H005',45,180,'E Block',25),
('H006',30,120,'F Block',10);

-- PlacementCompany
INSERT INTO PlacementCompany VALUES
('PC001','Infosys','IT Services','Software Developer, System Analyst','4-6 LPA','CSE, AIML','2025-05-15','Ravi Menon','ravi.hr@infosys.com'),
('PC002','TCS','IT Consulting','Developer, QA Engineer','3.5-5 LPA','CSE, ENTC','2025-05-20','Neha Kapoor','neha.k@tcs.com'),
('PC003','Larsen & Toubro','Engineering','Site Engineer, Project Manager','5-7 LPA','CIVIL, MECH','2025-06-01','Anil Rao','anil.rao@lnt.com'),
('PC004','Wipro','IT Services','Test Engineer, Developer','3.8-5.2 LPA','CSE, AIML','2025-05-18','Swati Das','swati.d@wipro.com'),
('PC005','HCL','Software Services','Support Engineer, Developer','3.2-4.5 LPA','CSE','2025-05-25','Kunal Verma','kunal.v@hcl.com'),
('PC006','Amazon','E‑commerce','SDE, Ops Manager','12-25 LPA','CSE, AIML','2025-06-10','Shreya Nair','shreya.n@amazon.com'),
('PC007','Google','Tech','SWE, UX Designer','20-40 LPA','CSE, AIML','2025-06-15','Aditya Jain','aditya.j@google.com'),
('PC008','Reliance','Energy','Plant Engineer','6-8 LPA','MECH, ENTC','2025-05-30','Mukul Shah','mukul.s@reliance.com'),
('PC009','ABB Robotics','Automation','Control Engineer, Robotics Engineer','8-12 LPA','RNA, MECH','2025-06-05','Ritika Sharma','ritika.s@abb.com'),
('PC010','Cognizant','IT','Software Engineer, QA','3.5-5.5 LPA','CSE, AIML, ENTC','2025-05-22','Sagar Desai','sagar.d@cognizant.com');

-- Transport
INSERT INTO Transport VALUES
('T001','Route A','Ramesh Yadav','MH01AB1234',40),
('T002','Route B','Suresh Kumar','DL05CD5678',35),
('T003','Route C','Anil Sharma','KA03EF9012',50),
('T004','Route D','Manoj Joshi','TN09GH3456',45),
('T005','Route E','Ravi Patel','GJ10IJ7890',40),
('T006','Route F','Ashok Meena','UP11KL1122',30),
('T007','Route G','Mukesh Nair','KL12MN3344',50),
('T008','Route H','Dinesh Reddy','AP13OP5566',35),
('T009','Route I','Ajay Verma','RJ14QR7788',40),
('T010','Route J','Vijay Singh','HR15ST9900',45);

-- Classroom
INSERT INTO Classroom VALUES
('CR001','C101',1,'Lecture',60,'Available'),
('CR002','C102',1,'Lab',30,'Occupied'),
('CR003','C103',1,'Lecture',50,'Available'),
('CR004','C201',2,'Lecture',60,'Occupied'),
('CR005','C202',2,'Lab',25,'Available'),
('CR006','C301',3,'Lecture',70,'Available'),
('CR007','C302',3,'Lab',20,'Occupied'),
('CR008','C401',4,'Lecture',65,'Available'),
('CR009','C402',4,'Lab',30,'Occupied'),
('CR010','C403',4,'Lecture',55,'Available');

INSERT INTO Book VALUES 
('B001', 'The Alchemist', 'Paulo Coelho', 'HarperOne', 'Available', NULL, NULL, 'L001'),
('B002', 'To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', 'Issued', '2024-01-15', '2024-01-29', 'L002'),
('B003', '1984', 'George Orwell', 'Secker & Warburg', 'Available', NULL, NULL, 'L003'),
('B004', 'Pride and Prejudice', 'Jane Austen', 'T. Egerton', 'Issued', '2024-02-01', '2024-02-15', 'L001'),
('B005', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Charles Scribner\'s Sons', 'Available', NULL, NULL, 'L002'),
('B006', 'Brave New World', 'Aldous Huxley', 'Chatto & Windus', 'Issued', '2024-02-10', '2024-02-24', 'L003'),
('B007', 'Moby Dick', 'Herman Melville', 'Harper & Brothers', 'Available', NULL, NULL, 'L001'),
('B008', 'Jane Eyre', 'Charlotte Brontë', 'Smith, Elder & Co.', 'Issued', '2024-03-05', '2024-03-19', 'L002'),
('B009', 'The Catcher in the Rye', 'J.D. Salinger', 'Little, Brown and Company', 'Available', NULL, NULL, 'L003'),
('B010', 'The Hobbit', 'J.R.R. Tolkien', 'George Allen & Unwin', 'Issued', '2024-03-10', '2024-03-24', 'L001');

INSERT INTO Inside VALUES
('S001','D001'),('S002','D002'),('S003','D003'),
('S004','D004'),('S005','D005'),('S006','D006'),
('S007','D007'),('S008','D008'),('S009','D009'),
('S010','D010');

-- Enrolls (Faculty ↔ Department)
INSERT INTO Enrolls VALUES
('F001','D001'),('F002','D002'),('F003','D003'),
('F004','D004'),('F005','D005'),('F006','D006'),
('F007','D007'),('F008','D008'),('F009','D009'),
('F010','D010');

-- Handles (Event ↔ Faculty)
INSERT INTO Handles VALUES
('E001','F001'),('E002','F003'),('E003','F005'),
('E004','F006'),('E005','F009'),('E006','F007'),
('E007','F001'),('E008','F010'),('E009','F008'),
('E010','F004');

-- Teaches (Student ↔ Faculty)
INSERT INTO Teaches VALUES
('S001','F001'),('S002','F002'),('S003','F003'),
('S004','F004'),('S005','F005'),('S006','F006'),
('S007','F007'),('S008','F008'),('S009','F009'),
('S010','F010');

-- Participates (Student ↔ Event)
INSERT INTO Participates VALUES
('E001','S001'),('E002','S003'),('E003','S005'),
('E004','S006'),('E005','S009'),('E006','S007'),
('E007','S001'),('E008','S010'),('E009','S008'),
('E010','S004');

-- View_Result (Student ↔ Result)
INSERT INTO View_Result VALUES
('R001','S001'),('R002','S002'),('R003','S003'),
('R004','S004'),('R005','S005'),('R006','S006'),
('R007','S007'),('R008','S008'),('R009','S009'),
('R010','S010');

-- 4) Show all tables to confirm
SHOW TABLES;