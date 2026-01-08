public enum Gender {
    FEMALE,
    MALE;

    public static Gender parse(String raw) {
        if (raw == null) throw new IllegalArgumentException("Gender is required.");

        String v = raw.trim();
        if (v.equalsIgnoreCase("F") || v.equalsIgnoreCase("FEMALE")) return FEMALE;
        if (v.equalsIgnoreCase("M") || v.equalsIgnoreCase("MALE")) return MALE;

        throw new IllegalArgumentException("Unknown gender (use M/F): " + raw);
    }
}
