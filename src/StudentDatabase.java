import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class StudentDatabase {
    private final List<Student> students = new ArrayList<>();

    public List<Student> getAll() {
        return List.copyOf(students);
    }

    public void add(Student student) {
        // Uniqueness constraints are common and make delete/search predictable
        if (findByStudentId(student.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("Student ID already exists: " + student.getStudentId());
        }
        if (findByPesel(student.getPesel()).isPresent()) {
            throw new IllegalArgumentException("PESEL already exists: " + student.getPesel());
        }
        students.add(student);
    }

    public Optional<Student> findByPesel(String pesel) {
        if (pesel == null) return Optional.empty();
        String p = pesel.trim();
        return students.stream()
                .filter(s -> s.getPesel().equals(p))
                .findFirst();
    }

    public Optional<Student> findByStudentId(String studentId) {
        if (studentId == null) return Optional.empty();
        String id = studentId.trim();
        return students.stream()
                .filter(s -> s.getStudentId().equals(id))
                .findFirst();
    }

    public List<Student> searchByLastName(String lastName) {
        if (lastName == null) return List.of();
        String ln = lastName.trim().toLowerCase(Locale.ROOT);
        return students.stream()
                .filter(s -> s.getLastName().toLowerCase(Locale.ROOT).equals(ln))
                .toList();
    }

    public boolean deleteByStudentId(String studentId) {
        Optional<Student> s = findByStudentId(studentId);
        s.ifPresent(students::remove);
        return s.isPresent();
    }

    public void sortByPesel() {
        students.sort(Comparator.comparing(Student::getPesel));
    }

    public void sortByLastName() {
        students.sort(
                Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Student::getFirstName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Student::getStudentId)
        );
    }

    public void replaceAll(List<Student> loaded) {
        students.clear();
        if (loaded != null) {
            students.addAll(loaded);
        }
    }
}

