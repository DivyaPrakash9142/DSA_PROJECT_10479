# PS 58 – College Management System

**Student:** Divya Prakash | **ERP:** RU-25-10479 | **Course:** Data Structures and Algorithms (RU-100-01-00029)
**Language:** Java (single file, no external libraries) | **Data file:** `college_data.txt`

## Problem
A console system to manage students, faculty and courses, with undo of the latest record change,
a queue of service requests, and a graph of course relationships.

## Run
```
javac CollegeManagementSystem.java
java CollegeManagementSystem            # uses college_data.txt in the current folder
java CollegeManagementSystem other.txt  # optional: use a different data file
```

## Data structures used
| Need | Structure | Where |
|---|---|---|
| Student / faculty / course records | Hand-written singly linked list (`SinglyLinkedList`) | add, insert-at-index (for undo), remove-at-index, search |
| Undo latest change | Linked stack (`LinkedStack<UndoAction>`) | each change pushes an action that reverses it |
| Service requests | Linked queue (`LinkedQueue`) | FIFO submit / process / view |
| Course relationships | Directed graph, adjacency lists in `ArrayList` (`CourseGraph`) | "course requires prerequisite"; BFS for all prerequisites; cycle check |
| Persistence | Text file | loaded at startup, saved on exit |

Record classes: `Student`, `Faculty`, `Course` (separate classes, all implement `HasId`).

## Features
1. Add student / faculty / course (IDs auto-generated and unique: `ST101`, `F101`, `C101`, ...)
2. Display and search (by ID or name) for each record type
3. Delete a student, faculty or course (a faculty member assigned to a course cannot be deleted)
4. Add a course prerequisite; display the course graph; list all (transitive) prerequisites using BFS
5. Submit, process and view service requests (queue)
6. Undo the latest supported change

**Undo covers** adding or deleting a student, faculty or course, and adding a prerequisite.
Service requests are not record changes, so they are not undone.

## Validation and safe handling
- Empty names, names over 60 characters, and the `|` character are rejected.
- Duplicate IDs are impossible (next ID = highest existing number + 1); duplicate IDs in a loaded file are skipped.
- Undo on an empty stack prints `Nothing to undo.`; processing an empty queue prints `No pending service requests.`
- Prerequisites: unknown course, self-prerequisite, duplicate edge, and circular dependency are all rejected.
- Missing data file = start empty; malformed lines are skipped with a warning count.
- The file is written to a temp file and renamed, so a crash cannot leave a half-written data file.
- Closing input (Ctrl+D / Ctrl+Z) also saves and exits.

## Test cases from the project description
| # | Input | Output |
|---|---|---|
| 1 Normal | Add student Aman | `Student added. ID: ST101 \| Aman` |
| 2 Boundary | Process next request with none pending | `No pending service requests.` |
| 3 Invalid | Search faculty F999 | `Faculty F999 not found.` |
| 4 Valid | Add Aman, add Riya, undo, display students | `Last operation undone: Add student Riya` then list shows only `ST101 \| Aman` |

All four were run and produce exactly the expected output.

## Data file format
```
STUDENT|ST101|Aman
FACULTY|F101|Dr Rao|CS
COURSE|C101|DSA|F101        (last field "-" when no faculty)
EDGE|C102|C101              (C102 requires C101)
REQUEST|Fix my ID card
```
