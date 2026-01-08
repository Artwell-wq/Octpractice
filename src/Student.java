import java.util.Objects;

public final class Student {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String studentId;
    private final String pesel;
    private final String gender; // "M" or "F"

    public Student(
            String firstName,
            String lastName,
            String address,
            String studentId,
            String pesel,
            String gender
    ) {
        this.firstName = requireNonBlank(firstName, "First name");
        this.lastName = requireNonBlank(lastName, "Last name");
        this.address = requireNonBlank(address, "Address");
        this.studentId = requireNonBlank(studentId, "Student ID");
        this.pesel = requireNonBlank(pesel, "PESEL");
        this.gender = normalizeGender(Objects.requireNonNull(gender, "Gender"));
    }

    private static String requireNonBlank(String v, String field) {
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " is required.");
        }
        return v.trim();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getPesel() {
        return pesel;
    }

    public String getGender() {
        return gender;
    }

    private static String normalizeGender(String g) {
        String v = g.trim().toUpperCase();
        if (!v.equals("M") && !v.equals("F")) {
            throw new IllegalArgumentException("Gender must be 'M' or 'F'.");
        }
        return v;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", studentId='" + studentId + '\'' +
                ", pesel='" + pesel + '\'' +
                ", gender=" + gender +
                '}';
    }
}
