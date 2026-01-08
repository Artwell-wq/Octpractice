public class Main {
    public static void main(String[] args) {
        StudentDatabase db = new StudentDatabase();
        java.nio.file.Path path = java.nio.file.Path.of("data", "students.csv");

        try {
            db.replaceAll(FileManager.loadFromFile(path));
            System.out.println("Loaded " + db.getAll().size() + " students.");
        } catch (Exception ex) {
            System.out.println("Warning: could not load database: " + ex.getMessage());
            System.out.println("Starting with an empty database.");
        }

        runMenu(db, path);
    }

    private static void runMenu(StudentDatabase db, java.nio.file.Path path) {
        java.util.Scanner in = new java.util.Scanner(System.in);
        try {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose option: ");
                String choice = in.nextLine().trim();

                try {
                    switch (choice) {
                        case "1":
                            addStudent(db, in);
                            break;
                        case "2":
                            displayAll(db);
                            break;
                        case "3":
                            searchByLastName(db, in);
                            break;
                        case "4":
                            searchByPesel(db, in);
                            break;
                        case "5":
                            db.sortByPesel();
                            System.out.println("Sorted by PESEL.");
                            break;
                        case "6":
                            db.sortByLastName();
                            System.out.println("Sorted by last name.");
                            break;
                        case "7":
                            deleteByStudentId(db, in);
                            break;
                        case "8":
                            save(db, path);
                            break;
                        case "9":
                            save(db, path);
                            running = false;
                            break;
                        default:
                            System.out.println("Unknown option. Try again.");
                            break;
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (java.io.IOException ex) {
                    System.out.println("I/O Error: " + ex.getMessage());
                }

                System.out.println();
            }
        } finally {
            in.close();
        }

        System.out.println("Bye.");
    }

    private static void printMenu() {
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

    private static void addStudent(StudentDatabase db, java.util.Scanner in) {
        System.out.print("First name: ");
        String firstName = in.nextLine();
        System.out.print("Last name: ");
        String lastName = in.nextLine();
        System.out.print("Address: ");
        String address = in.nextLine();
        System.out.print("Student ID: ");
        String studentId = in.nextLine();
        System.out.print("PESEL (11 digits): ");
        String pesel = in.nextLine();

        PeselValidator.validateOrThrow(pesel);

        // PESEL encodes gender, so we derive it automatically.
        String gender = PeselValidator.genderFromPesel(pesel);

        Student s = new Student(firstName, lastName, address, studentId, pesel.trim(), gender);
        db.add(s);

        java.time.LocalDate dob = PeselValidator.parseBirthDate(s.getPesel());
        System.out.println("Added: " + s.getFirstName() + " " + s.getLastName()
                + " (DOB " + dob + ", gender " + s.getGender() + ")");
    }

    private static void displayAll(StudentDatabase db) {
        java.util.List<Student> all = db.getAll();
        if (all.isEmpty()) {
            System.out.println("No students in database.");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-14s %-11s %-6s %s%n",
                "StudentID", "LastName", "FirstName", "PESEL", "DOB", "Sex", "Address");
        System.out.println("--------------------------------------------------------------------------------");

        for (Student s : all) {
            String dob = PeselValidator.parseBirthDate(s.getPesel()).toString();
            System.out.printf("%-10s %-12s %-12s %-14s %-11s %-6s %s%n",
                    s.getStudentId(),
                    s.getLastName(),
                    s.getFirstName(),
                    s.getPesel(),
                    dob,
                    s.getGender(),
                    s.getAddress()
            );
        }
    }

    private static void searchByLastName(StudentDatabase db, java.util.Scanner in) {
        System.out.print("Last name to search: ");
        String lastName = in.nextLine();
        java.util.List<Student> results = db.searchByLastName(lastName);

        if (results.isEmpty()) {
            System.out.println("No matches.");
            return;
        }

        for (Student s : results) {
            System.out.println(s.getStudentId() + ": " + s.getLastName() + " " + s.getFirstName() + " (PESEL " + s.getPesel() + ")");
        }
    }

    private static void searchByPesel(StudentDatabase db, java.util.Scanner in) {
        System.out.print("PESEL to search: ");
        String pesel = in.nextLine();
        Student s = db.findByPesel(pesel);
        if (s == null) {
            System.out.println("No match.");
        } else {
            System.out.println("Found: " + s);
        }
    }

    private static void deleteByStudentId(StudentDatabase db, java.util.Scanner in) {
        System.out.print("Student ID to delete: ");
        String id = in.nextLine();
        boolean deleted = db.deleteByStudentId(id);
        System.out.println(deleted ? "Deleted." : "No such Student ID.");
    }

    private static void save(StudentDatabase db, java.nio.file.Path path) throws java.io.IOException {
        FileManager.saveToFile(path, db.getAll());
        System.out.println("Saved " + db.getAll().size() + " students.");
    }
}