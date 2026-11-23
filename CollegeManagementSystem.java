import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * PS 58 - College Management System
 * Rungta International Skills University | DSA (RU-100-01-00029)
 *
 * Console application that manages students, faculty and courses.
 *   - Records       : hand-written singly linked list (SinglyLinkedList)
 *   - Undo          : linked Stack of UndoAction objects (LinkedStack)
 *   - Service reqs  : linked Queue of requests (LinkedQueue)
 *   - Courses       : directed prerequisite graph (CourseGraph, adjacency lists in ArrayList)
 *   - Persistence   : plain text file college_data.txt (loaded at start, saved on exit)
 *
 * Compile : javac CollegeManagementSystem.java
 * Run     : java CollegeManagementSystem [optional-data-file]
 */

// ---------------------------------------------------------------------------
// Record classes
// ---------------------------------------------------------------------------

interface HasId {
    String getId();
}

class Student implements HasId {
    private final String id;
    private final String name;

    Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    String getName() { return name; }

    @Override
    public String toString() { return id + " | " + name; }
}

class Faculty implements HasId {
    private final String id;
    private final String name;
    private final String department;

    Faculty(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public String getId() { return id; }
    String getName() { return name; }
    String getDepartment() { return department; }

    @Override
    public String toString() { return id + " | " + name + " | " + department; }
}

class Course implements HasId {
    private final String id;
    private final String title;
    private final String facultyId; // null when unassigned

    Course(String id, String title, String facultyId) {
        this.id = id;
        this.title = title;
        this.facultyId = facultyId;
    }

    public String getId() { return id; }
    String getTitle() { return title; }
    String getFacultyId() { return facultyId; }

    @Override
    public String toString() {
        return id + " | " + title + " | Faculty: " + (facultyId == null ? "unassigned" : facultyId);
    }
}

// ---------------------------------------------------------------------------
// Data structures
// ---------------------------------------------------------------------------

class Node<T> {
    T data;
    Node<T> next;

    Node(T data) { this.data = data; }
}

/** Singly linked list with head/tail pointers; used to store all records. */
class SinglyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    int size() { return size; }
    boolean isEmpty() { return size == 0; }

    void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    void insertAt(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == size) { addLast(value); return; }
        Node<T> node = new Node<>(value);
        if (index == 0) {
            node.next = head;
            head = node;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) prev = prev.next;
            node.next = prev.next;
            prev.next = node;
        }
        size++;
    }

    T removeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node<T> removed;
        if (index == 0) {
            removed = head;
            head = head.next;
            if (head == null) tail = null;
        } else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) prev = prev.next;
            removed = prev.next;
            prev.next = removed.next;
            if (removed == tail) tail = prev;
        }
        size--;
        return removed.data;
    }

    int indexOf(Predicate<T> test) {
        int i = 0;
        for (Node<T> cur = head; cur != null; cur = cur.next, i++) {
            if (test.test(cur.data)) return i;
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cur = head;
            public boolean hasNext() { return cur != null; }
            public T next() {
                if (cur == null) throw new NoSuchElementException();
                T v = cur.data;
                cur = cur.next;
                return v;
            }
        };
    }
}

/** LIFO stack on a linked list. pop()/peek() return null when empty instead of throwing. */
class LinkedStack<T> {
    private Node<T> top;
    private int size;

    void push(T value) {
        Node<T> node = new Node<>(value);
        node.next = top;
        top = node;
        size++;
    }

    T pop() {
        if (top == null) return null;
        T v = top.data;
        top = top.next;
        size--;
        return v;
    }

    boolean isEmpty() { return top == null; }
    int size() { return size; }
}

/** FIFO queue on a linked list. dequeue() returns null when empty instead of throwing. */
class LinkedQueue<T> implements Iterable<T> {
    private Node<T> front;
    private Node<T> rear;
    private int size;

    void enqueue(T value) {
        Node<T> node = new Node<>(value);
        if (rear == null) {
            front = rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    T dequeue() {
        if (front == null) return null;
        T v = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return v;
    }

    boolean isEmpty() { return front == null; }
    int size() { return size; }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cur = front;
            public boolean hasNext() { return cur != null; }
            public T next() {
                if (cur == null) throw new NoSuchElementException();
                T v = cur.data;
                cur = cur.next;
                return v;
            }
        };
    }
}

/** One undoable change: a description for the user and the action that reverses it. */
class UndoAction {
    final String description;
    final Runnable undo;

    UndoAction(String description, Runnable undo) {
        this.description = description;
        this.undo = undo;
    }
}

/**
 * Directed graph of course relationships. An edge (course -> prerequisite) means
 * "course requires prerequisite". Adjacency lists are ArrayLists.
 */
class CourseGraph {
    static final int OK = 0, SAME = 1, EXISTS = 2, CYCLE = 3;

    private final Map<String, ArrayList<String>> adj = new HashMap<>();

    void addCourse(String id) {
        if (!adj.containsKey(id)) adj.put(id, new ArrayList<String>());
    }

    /** Removes a course and every edge touching it; returns removed edges as {from, to} pairs. */
    List<String[]> removeCourse(String id) {
        List<String[]> removed = new ArrayList<>();
        ArrayList<String> out = adj.remove(id);
        if (out != null) {
            for (String to : out) removed.add(new String[] { id, to });
        }
        for (Map.Entry<String, ArrayList<String>> e : adj.entrySet()) {
            if (e.getValue().remove(id)) removed.add(new String[] { e.getKey(), id });
        }
        return removed;
    }

    int addEdge(String course, String prereq) {
        if (course.equals(prereq)) return SAME;
        ArrayList<String> out = adj.get(course);
        if (out.contains(prereq)) return EXISTS;
        // prereq already (transitively) depends on course -> new edge would close a cycle
        if (reachable(prereq).contains(course)) return CYCLE;
        out.add(prereq);
        return OK;
    }

    void restoreEdge(String course, String prereq) {
        addCourse(course);
        addCourse(prereq);
        ArrayList<String> out = adj.get(course);
        if (!out.contains(prereq)) out.add(prereq);
    }

    void removeEdge(String course, String prereq) {
        ArrayList<String> out = adj.get(course);
        if (out != null) out.remove(prereq);
    }

    List<String> directPrereqs(String id) {
        ArrayList<String> out = adj.get(id);
        return out == null ? new ArrayList<String>() : out;
    }

    /** Breadth-first traversal: every course that {@code id} depends on, directly or not. */
    List<String> reachable(String id) {
        List<String> result = new ArrayList<>();
        Map<String, Boolean> seen = new HashMap<>();
        LinkedQueue<String> queue = new LinkedQueue<>();
        seen.put(id, true);
        queue.enqueue(id);
        while (!queue.isEmpty()) {
            String cur = queue.dequeue();
            for (String next : directPrereqs(cur)) {
                if (!seen.containsKey(next)) {
                    seen.put(next, true);
                    result.add(next);
                    queue.enqueue(next);
                }
            }
        }
        return result;
    }
}

// ---------------------------------------------------------------------------
// Application
// ---------------------------------------------------------------------------

public class CollegeManagementSystem {

    /** Thrown when standard input ends, so the program can save and exit cleanly. */
    private static class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final String DEFAULT_FILE = "college_data.txt";
    private static final int MAX_TEXT = 60;

    private final SinglyLinkedList<Student> students = new SinglyLinkedList<>();
    private final SinglyLinkedList<Faculty> faculty = new SinglyLinkedList<>();
    private final SinglyLinkedList<Course> courses = new SinglyLinkedList<>();
    private final CourseGraph graph = new CourseGraph();
    private final LinkedStack<UndoAction> undoStack = new LinkedStack<>();
    private final LinkedQueue<String> requests = new LinkedQueue<>();

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final Path dataFile;

    CollegeManagementSystem(Path dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        Path file = Paths.get(args.length > 0 ? args[0] : DEFAULT_FILE);
        CollegeManagementSystem app = new CollegeManagementSystem(file);
        app.load();
        app.run();
    }

    // ------------------------------ menu ------------------------------

    private void run() {
        try {
            while (true) {
                printMenu();
                String choice = readLine("Enter choice: ").trim();
                if (choice.equals("0")) break;
                handle(choice);
            }
        } catch (EndOfInput e) {
            System.out.println();
        }
        save();
        System.out.println("Data saved to " + dataFile + ". Goodbye!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== College Management System =====");
        System.out.println(" 1. Add student            10. Delete record");
        System.out.println(" 2. Add faculty            11. Add course prerequisite");
        System.out.println(" 3. Add course             12. Display course graph");
        System.out.println(" 4. Display students       13. Show all prerequisites of a course");
        System.out.println(" 5. Display faculty        14. Submit service request");
        System.out.println(" 6. Display courses        15. Process next service request");
        System.out.println(" 7. Search student         16. View pending service requests");
        System.out.println(" 8. Search faculty         17. Undo last operation");
        System.out.println(" 9. Search course           0. Save and exit");
    }

    private void handle(String choice) {
        switch (choice) {
            case "1":  addStudent(); break;
            case "2":  addFaculty(); break;
            case "3":  addCourse(); break;
            case "4":  displayAll("Students", students); break;
            case "5":  displayAll("Faculty", faculty); break;
            case "6":  displayAll("Courses", courses); break;
            case "7":  search("Student", students); break;
            case "8":  search("Faculty", faculty); break;
            case "9":  search("Course", courses); break;
            case "10": deleteRecord(); break;
            case "11": addPrerequisite(); break;
            case "12": displayGraph(); break;
            case "13": showPrerequisites(); break;
            case "14": submitRequest(); break;
            case "15": processRequest(); break;
            case "16": viewRequests(); break;
            case "17": undo(); break;
            default:   System.out.println("Invalid choice. Enter a number from the menu.");
        }
    }

    // ------------------------------ input helpers ------------------------------

    private String readLine(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        try {
            String line = in.readLine();
            if (line == null) throw new EndOfInput();
            return line;
        } catch (IOException e) {
            throw new EndOfInput();
        }
    }

    /** Reads a non-empty text value; returns null (after printing why) if it is invalid. */
    private String readText(String prompt) {
        String text = readLine(prompt).trim();
        if (text.isEmpty()) {
            System.out.println("Invalid input: value cannot be empty.");
            return null;
        }
        if (text.length() > MAX_TEXT) {
            System.out.println("Invalid input: maximum " + MAX_TEXT + " characters.");
            return null;
        }
        if (text.indexOf('|') >= 0 || text.indexOf('\n') >= 0) {
            System.out.println("Invalid input: the '|' character is not allowed.");
            return null;
        }
        return text;
    }

    private static String normId(String s) {
        return s.trim().toUpperCase();
    }

    // ------------------------------ generic record helpers ------------------------------

    private <T extends HasId> int indexOfId(SinglyLinkedList<T> list, String id) {
        return list.indexOf(r -> r.getId().equalsIgnoreCase(id));
    }

    /** Next unique ID: prefix + (highest existing number + 1), starting at 101. */
    private <T extends HasId> String nextId(SinglyLinkedList<T> list, String prefix) {
        int max = 100;
        for (T r : list) {
            String id = r.getId();
            if (id.startsWith(prefix)) {
                try {
                    max = Math.max(max, Integer.parseInt(id.substring(prefix.length())));
                } catch (NumberFormatException ignored) {
                    // not one of our generated IDs
                }
            }
        }
        return prefix + (max + 1);
    }

    private <T extends HasId> void displayAll(String label, SinglyLinkedList<T> list) {
        if (list.isEmpty()) {
            System.out.println("No " + label.toLowerCase() + " found.");
            return;
        }
        System.out.println(label + " (" + list.size() + "):");
        int n = 1;
        for (T r : list) System.out.println("  " + (n++) + ". " + r);
    }

    private <T extends HasId> void search(String label, SinglyLinkedList<T> list) {
        String query = readLine("Enter " + label.toLowerCase() + " ID or name: ").trim();
        if (query.isEmpty()) {
            System.out.println("Invalid input: value cannot be empty.");
            return;
        }
        String q = query.toLowerCase();
        boolean found = false;
        for (T r : list) {
            // toString() starts with "ID | name ..." so a substring test covers ID and name
            if (r.getId().equalsIgnoreCase(query) || r.toString().toLowerCase().contains(q)) {
                if (!found) System.out.println(label + " found:");
                System.out.println("  " + r);
                found = true;
            }
        }
        if (!found) System.out.println(label + " " + query + " not found.");
    }

    // ------------------------------ add ------------------------------

    private void addStudent() {
        String name = readText("Enter student name: ");
        if (name == null) return;
        final String id = nextId(students, "ST");
        students.addLast(new Student(id, name));
        undoStack.push(new UndoAction("Add student " + name,
                () -> students.removeAt(indexOfId(students, id))));
        System.out.println("Student added. ID: " + id + " | " + name);
    }

    private void addFaculty() {
        String name = readText("Enter faculty name: ");
        if (name == null) return;
        String dept = readText("Enter department: ");
        if (dept == null) return;
        final String id = nextId(faculty, "F");
        faculty.addLast(new Faculty(id, name, dept));
        undoStack.push(new UndoAction("Add faculty " + name,
                () -> faculty.removeAt(indexOfId(faculty, id))));
        System.out.println("Faculty added. ID: " + id + " | " + name + " | " + dept);
    }

    private void addCourse() {
        String title = readText("Enter course title: ");
        if (title == null) return;
        String fid = normId(readLine("Assign faculty ID (leave blank for none): "));
        if (!fid.isEmpty() && indexOfId(faculty, fid) < 0) {
            System.out.println("Faculty " + fid + " not found. Course not added.");
            return;
        }
        final String facultyId = fid.isEmpty() ? null : fid;
        final String id = nextId(courses, "C");
        courses.addLast(new Course(id, title, facultyId));
        graph.addCourse(id);
        undoStack.push(new UndoAction("Add course " + title, () -> {
            graph.removeCourse(id);
            courses.removeAt(indexOfId(courses, id));
        }));
        System.out.println("Course added. ID: " + id + " | " + title);
    }

    // ------------------------------ delete ------------------------------

    private void deleteRecord() {
        String type = readLine("Record type (student/faculty/course): ").trim().toLowerCase();
        if (!type.equals("student") && !type.equals("faculty") && !type.equals("course")) {
            System.out.println("Invalid record type. Enter student, faculty or course.");
            return;
        }
        String id = normId(readLine("Enter " + type + " ID: "));
        if (id.isEmpty()) {
            System.out.println("Invalid input: value cannot be empty.");
            return;
        }
        switch (type) {
            case "student": deleteStudent(id); break;
            case "faculty": deleteFaculty(id); break;
            default:        deleteCourse(id);
        }
    }

    private void deleteStudent(String id) {
        final int idx = indexOfId(students, id);
        if (idx < 0) { System.out.println("Student " + id + " not found."); return; }
        final Student removed = students.removeAt(idx);
        undoStack.push(new UndoAction("Delete student " + removed.getName(),
                () -> students.insertAt(idx, removed)));
        System.out.println("Student " + removed.getId() + " deleted.");
    }

    private void deleteFaculty(String id) {
        final int idx = indexOfId(faculty, id);
        if (idx < 0) { System.out.println("Faculty " + id + " not found."); return; }
        List<String> teaching = new ArrayList<>();
        for (Course c : courses) {
            if (id.equalsIgnoreCase(c.getFacultyId())) teaching.add(c.getId());
        }
        if (!teaching.isEmpty()) {
            System.out.println("Cannot delete faculty " + id + ": assigned to course(s) "
                    + String.join(", ", teaching) + ". Delete those courses first.");
            return;
        }
        final Faculty removed = faculty.removeAt(idx);
        undoStack.push(new UndoAction("Delete faculty " + removed.getName(),
                () -> faculty.insertAt(idx, removed)));
        System.out.println("Faculty " + removed.getId() + " deleted.");
    }

    private void deleteCourse(String id) {
        final int idx = indexOfId(courses, id);
        if (idx < 0) { System.out.println("Course " + id + " not found."); return; }
        final Course removed = courses.removeAt(idx);
        final List<String[]> edges = graph.removeCourse(removed.getId());
        undoStack.push(new UndoAction("Delete course " + removed.getTitle(), () -> {
            courses.insertAt(idx, removed);
            graph.addCourse(removed.getId());
            for (String[] e : edges) graph.restoreEdge(e[0], e[1]);
        }));
        System.out.println("Course " + removed.getId() + " deleted.");
    }

    // ------------------------------ course graph ------------------------------

    private Course findCourse(String id) {
        int idx = indexOfId(courses, id);
        if (idx < 0) return null;
        int i = 0;
        for (Course c : courses) if (i++ == idx) return c;
        return null;
    }

    private void addPrerequisite() {
        String cid = normId(readLine("Enter course ID: "));
        Course course = findCourse(cid);
        if (course == null) { System.out.println("Course " + cid + " not found."); return; }
        String pid = normId(readLine("Enter prerequisite course ID: "));
        Course prereq = findCourse(pid);
        if (prereq == null) { System.out.println("Course " + pid + " not found."); return; }

        final String c = course.getId();
        final String p = prereq.getId();
        switch (graph.addEdge(c, p)) {
            case CourseGraph.OK:
                undoStack.push(new UndoAction("Add prerequisite " + c + " requires " + p,
                        () -> graph.removeEdge(c, p)));
                System.out.println("Prerequisite added: " + c + " requires " + p);
                break;
            case CourseGraph.SAME:
                System.out.println("A course cannot be its own prerequisite.");
                break;
            case CourseGraph.EXISTS:
                System.out.println("Prerequisite already exists: " + c + " requires " + p);
                break;
            default:
                System.out.println("Rejected: " + p + " already depends on " + c
                        + ", so this would create a circular dependency.");
        }
    }

    private void displayGraph() {
        if (courses.isEmpty()) { System.out.println("No courses found."); return; }
        System.out.println("Course relationships (course -> requires):");
        for (Course c : courses) {
            List<String> pre = graph.directPrereqs(c.getId());
            System.out.println("  " + c.getId() + " (" + c.getTitle() + ") -> "
                    + (pre.isEmpty() ? "no prerequisites" : String.join(", ", pre)));
        }
    }

    private void showPrerequisites() {
        String cid = normId(readLine("Enter course ID: "));
        Course c = findCourse(cid);
        if (c == null) { System.out.println("Course " + cid + " not found."); return; }
        List<String> all = graph.reachable(c.getId());
        if (all.isEmpty()) System.out.println(c.getId() + " has no prerequisites.");
        else System.out.println("All prerequisites of " + c.getId() + ": " + String.join(", ", all));
    }

    // ------------------------------ service requests ------------------------------

    private void submitRequest() {
        String text = readText("Enter service request: ");
        if (text == null) return;
        requests.enqueue(text);
        System.out.println("Service request submitted. Pending requests: " + requests.size());
    }

    private void processRequest() {
        String req = requests.dequeue();
        if (req == null) System.out.println("No pending service requests.");
        else System.out.println("Processed service request: " + req);
    }

    private void viewRequests() {
        if (requests.isEmpty()) { System.out.println("No pending service requests."); return; }
        System.out.println("Pending service requests (" + requests.size() + "):");
        int n = 1;
        for (String r : requests) System.out.println("  " + (n++) + ". " + r);
    }

    // ------------------------------ undo ------------------------------

    private void undo() {
        UndoAction action = undoStack.pop();
        if (action == null) {
            System.out.println("Nothing to undo.");
            return;
        }
        action.undo.run();
        System.out.println("Last operation undone: " + action.description);
    }

    // ------------------------------ file handling ------------------------------
    // Line formats (fields separated by '|'):
    //   STUDENT|id|name        FACULTY|id|name|department
    //   COURSE|id|title|facultyId-or-"-"        EDGE|courseId|prerequisiteId
    //   REQUEST|text

    private void load() {
        if (!Files.exists(dataFile)) {
            System.out.println("No saved data found (" + dataFile + "). Starting empty.");
            return;
        }
        int skipped = 0;
        try {
            List<String> lines = Files.readAllLines(dataFile, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                if (!loadLine(line.split("\\|", -1))) skipped++;
            }
        } catch (IOException e) {
            System.out.println("Could not read " + dataFile + ": " + e.getMessage()
                    + ". Starting empty.");
            return;
        }
        System.out.println("Loaded " + students.size() + " student(s), " + faculty.size()
                + " faculty, " + courses.size() + " course(s), " + requests.size()
                + " pending request(s) from " + dataFile + ".");
        if (skipped > 0) System.out.println("Warning: skipped " + skipped + " invalid line(s).");
    }

    private boolean loadLine(String[] f) {
        switch (f[0]) {
            case "STUDENT":
                if (f.length != 3 || indexOfId(students, f[1]) >= 0) return false;
                students.addLast(new Student(f[1], f[2]));
                return true;
            case "FACULTY":
                if (f.length != 4 || indexOfId(faculty, f[1]) >= 0) return false;
                faculty.addLast(new Faculty(f[1], f[2], f[3]));
                return true;
            case "COURSE":
                if (f.length != 4 || indexOfId(courses, f[1]) >= 0) return false;
                String fid = f[3].equals("-") ? null : f[3];
                if (fid != null && indexOfId(faculty, fid) < 0) return false;
                courses.addLast(new Course(f[1], f[2], fid));
                graph.addCourse(f[1]);
                return true;
            case "EDGE":
                if (f.length != 3 || findCourse(f[1]) == null || findCourse(f[2]) == null) return false;
                return graph.addEdge(f[1], f[2]) == CourseGraph.OK;
            case "REQUEST":
                if (f.length != 2) return false;
                requests.enqueue(f[1]);
                return true;
            default:
                return false;
        }
    }

    private void save() {
        Path tmp = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");
        try (BufferedWriter w = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
            for (Student s : students) line(w, "STUDENT", s.getId(), s.getName());
            for (Faculty f : faculty) line(w, "FACULTY", f.getId(), f.getName(), f.getDepartment());
            for (Course c : courses) {
                line(w, "COURSE", c.getId(), c.getTitle(), c.getFacultyId() == null ? "-" : c.getFacultyId());
            }
            for (Course c : courses) {
                for (String p : graph.directPrereqs(c.getId())) line(w, "EDGE", c.getId(), p);
            }
            for (String r : requests) line(w, "REQUEST", r);
            w.flush();
            // write-then-rename so a crash mid-save never leaves a half-written data file
            Files.move(tmp, dataFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: could not save data to " + dataFile + ": " + e.getMessage());
        }
    }

    private static void line(BufferedWriter w, String... fields) throws IOException {
        w.write(String.join("|", fields));
        w.newLine();
    }
}
