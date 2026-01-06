import java.util.Objects;

public final class Student {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String studentId;
    private final String pesel;
    private final Gender gender;

    public Student(
            String firstName,
            String lastName,
            String address,
            String studentId,
            String pesel,
            Gender gender
    ) {
        this.firstName = requireNonBlank(firstName, "First name");
        this.lastName = requireNonBlank(lastName, "Last name");
        this.address = requireNonBlank(address, "Address");
        this.studentId = requireNonBlank(studentId, "Student ID");
        this.pesel = requireNonBlank(pesel, "PESEL");
        this.gender = Objects.requireNonNull(gender, "Gender");
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

    public Gender getGender() {
        return gender;
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
