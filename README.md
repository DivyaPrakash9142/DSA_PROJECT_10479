# PS 58 – College Management System

🚀 Project Overview

The College Management System provides a secure and structured way to manage college-related information through a menu-driven console interface.

It uses different data structures according to their specific purposes and implements security mechanisms to protect sensitive information and prevent unauthorized access.


📌 About The Project

The College Management System is a menu-driven, console-based Java application developed to simplify college record management while demonstrating the practical implementation of Data Structures and Algorithms (DSA) and Cybersecurity principles.

The application manages student information, courses, marks, attendance, admission requests, and course prerequisites.

Unlike a basic management application, this project integrates multiple data structures and searching algorithms with security mechanisms such as password hashing, Role-Based Access Control (RBAC), account lockout, input validation, and tamper-evident audit logging.

It demonstrates how efficient programming and cybersecurity concepts can work together to build a structured and secure application.


🎓 College Management

Add, view, search, and delete student records.

Manage student marks and attendance.

Manage course information.

Search students using roll numbers and names.

Handle admission requests.

Maintain course prerequisite relationships.

🧠 Data Structures & Algorithms

ArrayList for student and course management.

LinkedList for recent activity tracking.

Stack for undoing the latest changes.

Queue for first-come-first-served admission processing.

Graph for course prerequisite management.

Binary Search for efficient roll number lookup.

Linear Search for searching student names.

BFS and DFS for graph traversal and study paths.

🔐 Cybersecurity Implementation

Salted password hashing using PBKDF2-HMAC-SHA256.

Role-Based Access Control (Admin, Faculty, Student).

Automatic account lockout after three failed login attempts.

Password strength validation.

Input validation to reject unsafe data.

SHA-256 hash-chained audit logging.

Audit log integrity verification.

Temporary password change enforcement for new student accounts.

🧠 Data Structures & Algorithms

Data Structure / Algorithm

Implementation

ArrayList

Stores students and courses

LinkedList

Maintains the last 10 recent activities

Stack

Implements Undo functionality

Queue

Processes admission requests using FIFO

Graph

Manages course prerequisites

Binary Search

Searches students by roll number

Linear Search

Searches students by name

BFS

Finds all course prerequisites

DFS

Generates a recommended study order

🔐 Cybersecurity Features

Password Hashing: PBKDF2-HMAC-SHA256 with a random 16-byte salt and 65,536 iterations.

Role-Based Access Control (RBAC): Separate permissions for Admin, Faculty, and Student.

Account Lockout: Locks accounts after 3 consecutive failed login attempts.

Input Validation: Rejects invalid formats and unsafe input characters.

Audit Logging: SHA-256 hash-chained audit log to detect unauthorized modifications.

Password Policy: Requires at least 8 characters, including uppercase, lowercase, numbers, and symbols.

🛠️ Technologies Used

Java

Java Collections Framework

Data Structures and Algorithms

Object-Oriented Programming (OOP)

File Handling

PBKDF2-HMAC-SHA256

SHA-256

Role-Based Access Control

🏗️ Project Architecture

The project is organized into modular Java classes, with each class responsible for a specific functionality.

Class

Responsibility

Student, Course, User

Data models

CollegeService

Student and course management

ActivityHistory

Recent activity using LinkedList

UndoManager, UndoAction

Undo functionality using Stack

AdmissionQueue

Admission processing using Queue

CourseGraph

Course prerequisites using Graph

AuthService

Authentication and account lockout

SecurityUtil

Password hashing and input validation

AccessControl

Role-based permissions

AuditLogger

Tamper-evident audit logging

Main

Main menu and application interface

DemoRunner

Automatic feature demonstration

⚙️ Installation & Execution

Prerequisites

Java Development Kit (JDK 8 or above)

VS Code / IntelliJ IDEA

Command Prompt or Terminal

Step 1: Clone the Repository

git clone https://github.com/YOUR-USERNAME/College-Management-System.git

Step 2: Open the Project Directory

cd College-Management-System

Step 3: Compile the Java Files

javac -d out src/*.java

Step 4: Run the Application

java -cp out Main

🔑 Default Login Credentials

The following accounts are created on the first execution:

Role

Username

Password

Admin

admin

Admin@123

Faculty

faculty1

Faculty@123

Student

RU-25-10479

Student@123

Note: These are demonstration credentials. Change default passwords before using the application with real data.

📂 Data Storage

The application automatically creates a data folder to store records.

File

Purpose

users.txt

User account information

students.txt

Student records

courses.txt

Course records

admissions.txt

Admission requests

prerequisites.txt

Course prerequisite relationships

audit.log

Hash-chained security audit log

📊 Time Complexity Analysis

Let:

N = Number of students

V = Number of courses

E = Number of prerequisite connections

L = Number of audit log entries

Operation

Time Complexity

Search by Roll Number

O(log N)

Search by Name

O(N)

Record Recent Activity

O(1)

Undo Operation

O(1)

Admission Queue Operations

O(1)

Add Prerequisite with Cycle Detection

O(V + E)

BFS / DFS Traversal

O(V + E)

Audit Log Integrity Check

O(L)

🔮 Future Enhancements

The following features can be considered for future versions:

MySQL Database Integration

Graphical User Interface using Java Swing or JavaFX

Two-Factor Authentication (2FA)

AES Encryption for stored data

Fee and Timetable Management

Priority Queue for merit-based admissions

Web-Based College Management System

Campus Navigation using Dijkstra's Algorithm

🎯 Learning Outcomes

Through this project, I gained practical experience in:

Implementing different data structures in Java.

Applying Binary Search, Linear Search, BFS, and DFS.

Understanding Stack, Queue, LinkedList, and Graph operations.

Applying Object-Oriented Programming concepts.

Implementing password hashing and authentication.

Understanding Role-Based Access Control.

Applying cybersecurity principles and secure coding practices.

Analyzing algorithm time complexity.


