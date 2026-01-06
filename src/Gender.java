public enum Gender {
    FEMALE,
    MALE;

    public static Gender parse(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Gender is required.");
        }
        String v = raw.trim().toUpperCase();
        return switch (v) {
            case "F", "FEMALE", "K", "KOBIETA" -> FEMALE;
            case "M", "MALE", "MAN", "MEZCZYZNA", "MĘŻCZYZNA" -> MALE;
            default -> throw new IllegalArgumentException("Unknown gender: " + raw);
        };
    }
}
