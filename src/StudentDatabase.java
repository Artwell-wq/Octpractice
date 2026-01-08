import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class StudentDatabase {
    private final List<Student> students = new ArrayList<>();

    public List<Student> getAll() {
        // Return a copy so callers can't modify our internal list (encapsulation).
        return new ArrayList<Student>(students);
    }

    public void add(Student student) {
        // Uniqueness constraints are common and make delete/search predictable
        if (findByStudentId(student.getStudentId()) != null) {
            throw new IllegalArgumentException("Student ID already exists: " + student.getStudentId());
        }
        if (findByPesel(student.getPesel()) != null) {
            throw new IllegalArgumentException("PESEL already exists: " + student.getPesel());
        }
        students.add(student);
    }

    public Student findByPesel(String pesel) {
        if (pesel == null) return null;
        String p = pesel.trim();
        for (Student s : students) {
            if (s.getPesel().equals(p)) {
                return s;
            }
        }
        return null;
    }

    public Student findByStudentId(String studentId) {
        if (studentId == null) return null;
        String id = studentId.trim();
        for (Student s : students) {
            if (s.getStudentId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public List<Student> searchByLastName(String lastName) {
        if (lastName == null) return new ArrayList<Student>();
        String ln = lastName.trim().toLowerCase(Locale.ROOT);
        List<Student> results = new ArrayList<Student>();
        for (Student s : students) {
            if (s.getLastName().toLowerCase(Locale.ROOT).equals(ln)) {
                results.add(s);
            }
        }
        return results;
    }

    public boolean deleteByStudentId(String studentId) {
        Student s = findByStudentId(studentId);
        if (s == null) return false;
        students.remove(s);
        return true;
    }

    public void sortByPesel() {
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return a.getPesel().compareTo(b.getPesel());
            }
        });
    }

    public void sortByLastName() {
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                int c1 = a.getLastName().compareToIgnoreCase(b.getLastName());
                if (c1 != 0) return c1;

                int c2 = a.getFirstName().compareToIgnoreCase(b.getFirstName());
                if (c2 != 0) return c2;

                return a.getStudentId().compareTo(b.getStudentId());
            }
        });
    }

    public void replaceAll(List<Student> loaded) {
        students.clear();
        if (loaded != null) {
            students.addAll(loaded);
        }
    }
}

