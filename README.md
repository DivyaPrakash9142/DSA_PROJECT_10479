#🎓 PS:58 College Management System

## 🚀 Project Overview

The College Management System provides a secure and structured way to manage college-related information through a menu-driven console interface.

It uses different data structures according to their specific purposes and implements security mechanisms to protect sensitive information and prevent unauthorized access.


## 📌 About The Project

The **College Management System** is a menu-driven, console-based Java application developed to simplify college record management while demonstrating the practical implementation of Data Structures and Algorithms (DSA) and Cybersecurity principles.

The application manages student information, courses, marks, attendance, admission requests, and course prerequisites.

Unlike a basic management application, this project integrates multiple data structures and searching algorithms with security mechanisms such as password hashing, Role-Based Access Control (RBAC), account lockout, input validation, and tamper-evident audit logging.

It demonstrates how efficient programming and cybersecurity concepts can work together to build a structured and secure application.

## ✨ Key Features

### 🎓 College Management

- Add, view, search, and delete student records.
- Manage student marks and attendance.
- Manage course information.
- Search students using roll numbers and names.
- Handle admission requests.
- Maintain course prerequisite relationships.

### 🧠 Data Structures & Algorithms

- ArrayList for student and course management.
- LinkedList for recent activity tracking.
- Stack for undoing the latest changes.
- Queue for first-come-first-served admission processing.
- Graph for course prerequisite management.
- Binary Search for efficient roll number lookup.
- Linear Search for searching student names.
- BFS and DFS for graph traversal and study paths.

### 🔐 Cybersecurity Implementation

- Salted password hashing using PBKDF2-HMAC-SHA256.
- Role-Based Access Control (Admin, Faculty, Student).
- Automatic account lockout after three failed login attempts.
- Password strength validation.
- Input validation to reject unsafe data.
- SHA-256 hash-chained audit logging.
- Audit log integrity verification.
- Temporary password change enforcement for new student accounts.


## 🛠️ Technology Stack

| Technology                 | Purpose                              |
| -------------------------- | ------------------------------------ |
| Java                       | Core application development         |
| Java Collections Framework | Data structure implementation        |
| ArrayList                  | Student and course storage           |
| LinkedList                 | Activity history and admission queue |
| Stack                      | Undo operations                      |
| Queue                      | Admission management                 |
| Graph                      | Course prerequisite management       |
| BFS & DFS                  | Graph traversal                      |
| Binary Search              | Student lookup                       |
| File Handling              | Persistent data storage              |
| PBKDF2-HMAC-SHA256         | Password protection                  |
| SHA-256                    | Audit log integrity                  |


### Core Modules

| Module          | Responsibility                          |
| --------------- | --------------------------------------- |
| Main            | Menu-driven application interface       |
| AuthService     | User authentication and account lockout |
| CollegeService  | Student and course operations           |
| SecurityUtil    | Password hashing and validation         |
| AccessControl   | Role-based permissions                  |
| ActivityHistory | Recent activity management              |
| UndoManager     | Reversing recent changes                |
| AdmissionQueue  | Admission request processing            |
| CourseGraph     | Course prerequisite management          |
| AuditLogger     | Security event logging                  |


## 📚 Data Structures & Algorithms

One of the main objectives of this project is to demonstrate the practical application of different data structures.

| Data Structure | Application                | Operations                |
| -------------- | -------------------------- | ------------------------- |
| ArrayList      | Student and course records | Add, Get, Sort            |
| LinkedList     | Recent activity history    | addFirst(), removeLast()  |
| Stack          | Undo functionality         | push(), pop(), peek()     |
| Queue          | Admission requests         | offer(), poll(), peek()   |
| Graph          | Course prerequisites       | BFS, DFS, Cycle Detection |

### 🔍 Searching Algorithms

**1. Binary Search**

Used to search students by roll number. The student records are maintained in sorted order, allowing efficient searching.

Time Complexity: `O(log N)`

**2. Linear Search**

Used to search students by name, including partial name matches.

Time Complexity: `O(N)`

### 🌐 Graph Algorithms

- **Breadth-First Search (BFS):** Finds all prerequisites of a selected course.
- **Depth-First Search (DFS):** Generates a recommended study order.
- **Cycle Detection:** Prevents invalid circular prerequisite relationships.


## 🔐 Cybersecurity Architecture

Security is an important part of this application.

| Security Threat     | Security Control                      |
| ------------------- | ------------------------------------- |
| Password theft      | Salted PBKDF2-HMAC-SHA256 hashing     |
| Weak passwords      | Password complexity validation        |
| Brute-force attacks | Lockout after 3 failed attempts       |
| Unauthorized access | Role-Based Access Control             |
| Invalid input       | Regex-based input validation          |
| Audit log tampering | SHA-256 hash chaining                 |
| Default passwords   | Temporary password change requirement |

### CIA Triad Implementation

- **Confidentiality:** Password hashing and role-based permissions.
- **Integrity:** Input validation and tamper-evident audit logging.
- **Availability:** Persistent file storage and administrative account unlocking.


## ⚙️ Installation & Execution

### Prerequisites

- Java Development Kit (JDK 8 or above).
- VS Code or IntelliJ IDEA.
- Command Prompt or Terminal.

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/College-Management-System.git
```

### 2. Navigate to the Project Directory

```bash
cd College-Management-System
```

### 3. Compile the Application

```bash
javac -d out src/*.java
```

### 4. Run the Application

```bash
java -cp out Main
```

---

## 🔑 Default Login Credentials

The application creates demonstration accounts on its first execution.

| Role    | Username    | Password     |
| ------- | ----------- | ------------ |
| Admin   | admin       | Admin\@123   |
| Faculty | faculty1    | Faculty\@123 |
| Student | RU-25-10479 | Student\@123 |



## 💾 File Storage System

The application uses file handling to save and retrieve information.

| File              | Description              |
| ----------------- | ------------------------ |
| users.txt         | User account information |
| students.txt      | Student records          |
| courses.txt       | Course information       |
| admissions.txt    | Admission requests       |
| prerequisites.txt | Course prerequisite data |
| audit.log         | Security audit records   |

All these files are maintained inside the runtime `data` directory.

---

## 📊 Time Complexity Analysis

Let:

- N = Number of students
- V = Number of courses
- E = Number of prerequisite connections
- L = Number of audit log entries

| Operation                     | Time Complexity |
| ----------------------------- | --------------- |
| Search by Roll Number         | O(log N)        |
| Search by Name                | O(N)            |
| Record Activity               | O(1)            |
| Undo Operation                | O(1)            |
| Queue Operations              | O(1)            |
| Graph Cycle Detection         | O(V + E)        |
| BFS Traversal                 | O(V + E)        |
| DFS Traversal                 | O(V + E)        |
| Audit Integrity Check         | O(L)            |
| Display / Load / Save Records | O(N)            |


## 🧪 Testing & Demonstration

The project report documents the following test scenarios:

| Test Case             | Expected Result                   |
| --------------------- | --------------------------------- |
| Add Student           | Student record successfully added |
| Search by Roll Number | Student found using Binary Search |
| Invalid Roll Number   | Input rejected                    |
| Student Not Found     | Appropriate message displayed     |
| Three Failed Logins   | Account locked                    |
| Admission Request     | Added to Queue                    |
| Process Admission     | FIFO processing                   |
| Undo Operation        | Latest change reversed            |
| Course Prerequisites  | BFS and DFS results generated     |

### Suggested Demo Workflow

1. Login as Admin.
2. Add a new student.
3. Search for the student by roll number.
4. Update marks and attendance.
5. Test the Undo feature.
6. Add and process admission requests.
7. Explore course prerequisites.
8. Check recent activity and audit log.


## 🚀 Future Enhancements

The following features can be considered for future versions:

- MySQL Database Integration
- Graphical User Interface using Java Swing or JavaFX
- Two-Factor Authentication (2FA)
- AES Encryption for stored data
- Fee and Timetable Management
- Priority Queue for merit-based admissions
- Web-Based College Management System
- Campus Navigation using Dijkstra's Algorithm


## 🎯 Learning Outcomes

This project helped me understand and implement:

- Core Java and Object-Oriented Programming.
- Java Collections Framework.
- Fundamental Data Structures and Algorithms.
- Searching and graph traversal algorithms.
- Time complexity analysis.
- File handling and data persistence.
- Password hashing and authentication.
- Role-Based Access Control.
- Secure input validation.
- Audit logging and data integrity.
