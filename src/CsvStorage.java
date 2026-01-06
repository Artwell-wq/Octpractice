import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CsvStorage {
    private CsvStorage() {}

    private static final String HEADER = "firstName,lastName,address,studentId,pesel,gender";

    public static void save(Path path, List<Student> students) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            w.write(HEADER);
            w.newLine();
            for (Student s : students) {
                w.write(csv(s.getFirstName()));
                w.write(',');
                w.write(csv(s.getLastName()));
                w.write(',');
                w.write(csv(s.getAddress()));
                w.write(',');
                w.write(csv(s.getStudentId()));
                w.write(',');
                w.write(csv(s.getPesel()));
                w.write(',');
                w.write(csv(s.getGender().name()));
                w.newLine();
            }
        }
    }

    public static List<Student> load(Path path) throws IOException {
        if (!Files.exists(path)) {
            return List.of();
        }
        List<Student> out = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = r.readLine(); // may be null
            if (header == null) return List.of();

            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);
                if (cols.size() != 6) {
                    throw new IOException("Invalid CSV row (expected 6 columns): " + line);
                }
                String firstName = cols.get(0);
                String lastName = cols.get(1);
                String address = cols.get(2);
                String studentId = cols.get(3);
                String pesel = cols.get(4);
                Gender gender = Gender.valueOf(cols.get(5));

                // Keep file loads strict too:
                PeselValidator.validateOrThrow(pesel);
                Gender gFromPesel = PeselValidator.genderFromPesel(pesel);
                if (gender != gFromPesel) {
                    throw new IOException("Gender in file does not match PESEL for studentId=" + studentId);
                }

                out.add(new Student(firstName, lastName, address, studentId, pesel, gender));
            }
        }
        return out;
    }

    // Minimal CSV quoting: always quote if special chars exist.
    private static String csv(String value) {
        String v = value == null ? "" : value;
        boolean needs = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        if (!needs) return v;
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    private static List<String> parseCsvLine(String line) throws IOException {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    cols.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }
        if (inQuotes) {
            throw new IOException("Unterminated CSV quotes: " + line);
        }
        cols.add(cur.toString());
        return cols;
    }
}

