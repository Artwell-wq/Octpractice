import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public final class ConsoleApp {
    private final StudentDatabase db;
    private final Path storagePath;
    private final Scanner in;

    public ConsoleApp(StudentDatabase db, Path storagePath, Scanner in) {
        this.db = db;
        this.storagePath = storagePath;
        this.in = in;
    }

    public void run() {
        System.out.println("Academic Student Database");
        System.out.println("Storage file: " + storagePath.toAbsolutePath());

        boolean running = true;
        while (running) {
            printMenu();
            String choice = prompt("Choose option").trim();
            try {
                switch (choice) {
                    case "1" -> addStudent();
                    case "2" -> displayAll();
                    case "3" -> searchByLastName();
                    case "4" -> searchByPesel();
                    case "5" -> {
                        db.sortByPesel();
                        System.out.println("Sorted by PESEL.");
                    }
                    case "6" -> {
                        db.sortByLastName();
                        System.out.println("Sorted by last name.");
                    }
                    case "7" -> deleteByStudentId();
                    case "8" -> save();
                    case "9" -> {
                        save();
                        running = false;
                    }
                    default -> System.out.println("Unknown option. Try again.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("I/O Error: " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Bye.");
    }

    private void printMenu() {
        System.out.println("--------------------------------------------------");
        System.out.println("1) Add new student");
        System.out.println("2) Display all students");
        System.out.println("3) Search by last name");
        System.out.println("4) Search by PESEL");
        System.out.println("5) Sort by PESEL");
        System.out.println("6) Sort by last name");
        System.out.println("7) Delete by Student ID");
        System.out.println("8) Save database");
        System.out.println("9) Save and exit");
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return in.nextLine();
    }

    private void addStudent() {
        String firstName = prompt("First name");
        String lastName = prompt("Last name");
        String address = prompt("Address");
        String studentId = prompt("Student ID");
        String pesel = prompt("PESEL (11 digits)");
        PeselValidator.validateOrThrow(pesel);

        Gender derived = PeselValidator.genderFromPesel(pesel);
        String genderRaw = prompt("Gender (M/F) [optional; blank = auto from PESEL]").trim();
        Gender gender = derived;
        if (!genderRaw.isEmpty()) {
            gender = Gender.parse(genderRaw);
            if (gender != derived) {
                throw new IllegalArgumentException("Gender does not match PESEL (PESEL implies " + derived + ").");
            }
        }

        Student s = new Student(firstName, lastName, address, studentId, pesel.trim(), gender);
        db.add(s);

        System.out.println("Added: " + s.getFirstName() + " " + s.getLastName()
                + " (DOB " + PeselValidator.parseBirthDate(s.getPesel()).format(DateTimeFormatter.ISO_DATE) + ")");
    }

    private void displayAll() {
        List<Student> all = db.getAll();
        if (all.isEmpty()) {
            System.out.println("No students in database.");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-14s %-11s %-6s %s%n",
                "StudentID", "LastName", "FirstName", "PESEL", "DOB", "Sex", "Address");
        System.out.println("--------------------------------------------------------------------------------");
        for (Student s : all) {
            String dob = PeselValidator.parseBirthDate(s.getPesel()).format(DateTimeFormatter.ISO_DATE);
            System.out.printf("%-10s %-12s %-12s %-14s %-11s %-6s %s%n",
                    s.getStudentId(),
                    s.getLastName(),
                    s.getFirstName(),
                    s.getPesel(),
                    dob,
                    s.getGender().name(),
                    s.getAddress()
            );
        }
    }

    private void searchByLastName() {
        String lastName = prompt("Last name to search");
        List<Student> results = db.searchByLastName(lastName);
        if (results.isEmpty()) {
            System.out.println("No matches.");
            return;
        }
        results.forEach(s -> System.out.println(s.getStudentId() + ": " + s.getLastName() + " " + s.getFirstName() + " (PESEL " + s.getPesel() + ")"));
    }

    private void searchByPesel() {
        String pesel = prompt("PESEL to search");
        db.findByPesel(pesel).ifPresentOrElse(
                s -> System.out.println("Found: " + s),
                () -> System.out.println("No match.")
        );
    }

    private void deleteByStudentId() {
        String id = prompt("Student ID to delete");
        boolean deleted = db.deleteByStudentId(id);
        System.out.println(deleted ? "Deleted." : "No such Student ID.");
    }

    public void save() throws IOException {
        CsvStorage.save(storagePath, db.getAll());
        System.out.println("Saved " + db.getAll().size() + " students.");
    }
}

