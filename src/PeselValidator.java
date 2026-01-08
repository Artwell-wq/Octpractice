import java.time.DateTimeException;
import java.time.LocalDate;

public final class PeselValidator {
    private PeselValidator() {}

    private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    public static boolean isValid(String pesel) {
        try {
            validateOrThrow(pesel);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static void validateOrThrow(String pesel) {
        String p = normalize(pesel);
        if (p.length() != 11) {
            throw new IllegalArgumentException("PESEL must be exactly 11 digits.");
        }
        for (int i = 0; i < p.length(); i++) {
            if (!Character.isDigit(p.charAt(i))) {
                throw new IllegalArgumentException("PESEL must contain only digits.");
            }
        }
        if (!checksumOk(p)) {
            throw new IllegalArgumentException("Invalid PESEL checksum.");
        }
        // Validate encoded date (common PESEL rule)
        parseBirthDate(p); // throws if invalid date
    }

    /**
     * PESEL encodes gender in the 10th digit:
     * - odd  -> male
     * - even -> female
     *
     * @return "M" or "F"
     */
    public static String genderFromPesel(String pesel) {
        String p = normalize(pesel);
        if (p.length() != 11) {
            throw new IllegalArgumentException("PESEL must be exactly 11 digits.");
        }
        char genderDigit = p.charAt(9); // 10th digit (0-based index 9)
        int d = genderDigit - '0';
        return (d % 2 == 0) ? "F" : "M";
    }

    public static LocalDate parseBirthDate(String pesel) {
        String p = normalize(pesel);
        if (p.length() < 6) {
            throw new IllegalArgumentException("PESEL too short to contain date.");
        }

        int yy = Integer.parseInt(p.substring(0, 2));
        int mm = Integer.parseInt(p.substring(2, 4));
        int dd = Integer.parseInt(p.substring(4, 6));

        int yearBase;
        int month;

        if (mm >= 81 && mm <= 92) { // 1800-1899
            yearBase = 1800;
            month = mm - 80;
        } else if (mm >= 1 && mm <= 12) { // 1900-1999
            yearBase = 1900;
            month = mm;
        } else if (mm >= 21 && mm <= 32) { // 2000-2099
            yearBase = 2000;
            month = mm - 20;
        } else if (mm >= 41 && mm <= 52) { // 2100-2199
            yearBase = 2100;
            month = mm - 40;
        } else if (mm >= 61 && mm <= 72) { // 2200-2299
            yearBase = 2200;
            month = mm - 60;
        } else {
            throw new IllegalArgumentException("Invalid PESEL month encoding.");
        }

        int year = yearBase + yy;

        try {
            return LocalDate.of(year, month, dd);
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Invalid PESEL date.", ex);
        }
    }

    private static boolean checksumOk(String p) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (p.charAt(i) - '0') * WEIGHTS[i];
        }
        int check = (10 - (sum % 10)) % 10;
        int last = p.charAt(10) - '0';
        return check == last;
    }

    private static String normalize(String pesel) {
        if (pesel == null) {
            throw new IllegalArgumentException("PESEL is required.");
        }
        return pesel.trim();
    }
}

