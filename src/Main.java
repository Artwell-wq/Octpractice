public class Main {
    public static void main(String[] args) {
        var db = new StudentDatabase();
        var path = java.nio.file.Path.of("data", "students.csv");

        try {
            db.replaceAll(CsvStorage.load(path));
            System.out.println("Loaded " + db.getAll().size() + " students.");
        } catch (Exception ex) {
            System.out.println("Warning: could not load database: " + ex.getMessage());
            System.out.println("Starting with an empty database.");
        }

        try (var scanner = new java.util.Scanner(System.in)) {
            new ConsoleApp(db, path, scanner).run();
        }
    }
}