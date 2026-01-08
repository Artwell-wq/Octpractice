import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Beginner-friendly file storage.
 * Saves/loads the database using a simple CSV file.
 */
public final class FileManager {
    private FileManager() {}

    private static final String HEADER = "firstName,lastName,address,studentId,pesel,gender";

    public static void saveToFile(Path path, List<Student> students) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        try {
            w.write(HEADER);
            w.newLine();

            for (Student s : students) {
                w.write(escape(s.getFirstName()));
                w.write(",");
                w.write(escape(s.getLastName()));
                w.write(",");
                w.write(escape(s.getAddress()));
                w.write(",");
                w.write(escape(s.getStudentId()));
                w.write(",");
                w.write(escape(s.getPesel()));
                w.write(",");
                w.write(escape(s.getGender()));
                w.newLine();
            }
        } finally {
            w.close();
        }
    }

    public static List<Student> loadFromFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            return new ArrayList<Student>();
        }

        List<Student> out = new ArrayList<Student>();

        BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        try {
            String header = r.readLine(); // may be null
            if (header == null) return out;

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
                String gender = cols.get(5);

                // Validate PESEL and keep file strict:
                PeselValidator.validateOrThrow(pesel);
                String gFromPesel = PeselValidator.genderFromPesel(pesel);
                if (!gFromPesel.equalsIgnoreCase(gender.trim())) {
                    throw new IOException("Gender in file does not match PESEL for studentId=" + studentId);
                }

                out.add(new Student(firstName, lastName, address, studentId, pesel, gender));
            }
        } finally {
            r.close();
        }

        return out;
    }

    // CSV escaping (minimal): quote if special chars exist.
    private static String escape(String value) {
        String v = value == null ? "" : value;
        boolean needs = v.indexOf(',') >= 0 || v.indexOf('"') >= 0 || v.indexOf('\n') >= 0 || v.indexOf('\r') >= 0;
        if (!needs) return v;
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }

    private static List<String> parseCsvLine(String line) throws IOException {
        List<String> cols = new ArrayList<String>();
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

