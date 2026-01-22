/**
 * Binary Sequences Combinatorics Problems
 * 
 * Three problems about binary sequences of length 10:
 * 1. How many have equal zeros and ones? (5 zeros, 5 ones)
 * 2. How many have more ones than zeros?
 * 3. How many have exactly 3 zeros with no two zeros adjacent?
 */
public class Main {
    
    // Calculate binomial coefficient C(n,k)
    public static long binomial(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        // Use symmetry for efficiency
        if (k > n - k) k = n - k;
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }
    
    // Count zeros in a binary string
    public static int countZeros(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '0') count++;
        }
        return count;
    }
    
    // Check if no two zeros are adjacent
    public static boolean noAdjacentZeros(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '0' && s.charAt(i + 1) == '0') {
                return false;
            }
        }
        return true;
    }
    
    // Generate binary string from number
    public static String toBinary(int num, int length) {
        String s = Integer.toBinaryString(num);
        while (s.length() < length) {
            s = "0" + s;
        }
        return s;
    }
    
    public static void main(String[] args) {
        int n = 10;  // sequence length
        
        System.out.println("======================================================================");
        System.out.println("       BINARY SEQUENCES OF LENGTH 10 - COMBINATORICS PROBLEMS");
        System.out.println("======================================================================");
        System.out.println();
        
        // =====================================================================
        // PROBLEM 1: Equal number of zeros and ones
        // =====================================================================
        System.out.println("======================================================================");
        System.out.println("PROBLEM 1: How many sequences have EQUAL zeros and ones?");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  For length 10 with equal zeros and ones, we need 5 zeros and 5 ones.");
        System.out.println();
        System.out.println("  SOLUTION: Choose which 5 positions (out of 10) get zeros.");
        System.out.println("            The remaining 5 positions get ones.");
        System.out.println();
        System.out.println("  Formula: C(10, 5) = 10! / (5! * 5!)");
        System.out.printf("           C(10, 5) = %d%n", binomial(10, 5));
        System.out.println();
        
        // Verify by brute force
        int countEqual = 0;
        for (int i = 0; i < (1 << n); i++) {
            String binary = toBinary(i, n);
            if (countZeros(binary) == 5) {
                countEqual++;
            }
        }
        System.out.println("  Verification (brute force): " + countEqual);
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |  ANSWER 1: " + binomial(10, 5) + " sequences                  |");
        System.out.println("  +------------------------------------------+");
        System.out.println();
        
        // =====================================================================
        // PROBLEM 2: More ones than zeros
        // =====================================================================
        System.out.println("======================================================================");
        System.out.println("PROBLEM 2: How many sequences have MORE ONES than zeros?");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  More ones than zeros means: #ones > #zeros");
        System.out.println("  For length 10: ones >= 6 (so zeros <= 4)");
        System.out.println();
        System.out.println("  SOLUTION: Sum C(10,k) for k = 0,1,2,3,4 zeros");
        System.out.println("            (equivalently, for 6,7,8,9,10 ones)");
        System.out.println();
        System.out.println("  Breakdown:");
        long totalMoreOnes = 0;
        for (int zeros = 0; zeros <= 4; zeros++) {
            int ones = 10 - zeros;
            long count = binomial(10, zeros);
            totalMoreOnes += count;
            System.out.printf("    %d zeros, %d ones: C(10,%d) = %d%n", zeros, ones, zeros, count);
        }
        System.out.println("    ─────────────────────────────");
        System.out.printf("    Total: %d%n", totalMoreOnes);
        System.out.println();
        
        // Alternative calculation using symmetry
        System.out.println("  ALTERNATIVE METHOD (using symmetry):");
        System.out.println("    Total sequences = 2^10 = " + (1 << n));
        System.out.println("    Sequences with equal = C(10,5) = " + binomial(10, 5));
        System.out.println("    Sequences with more ones = Sequences with more zeros (symmetry)");
        System.out.println("    So: more ones = (2^10 - C(10,5)) / 2");
        System.out.printf("                  = (%d - %d) / 2%n", (1 << n), binomial(10, 5));
        System.out.printf("                  = %d / 2 = %d%n", (1 << n) - binomial(10, 5), 
                          ((1 << n) - binomial(10, 5)) / 2);
        System.out.println();
        
        // Verify by brute force
        int countMoreOnes = 0;
        for (int i = 0; i < (1 << n); i++) {
            String binary = toBinary(i, n);
            int zeros = countZeros(binary);
            if (zeros < 5) {  // more ones than zeros
                countMoreOnes++;
            }
        }
        System.out.println("  Verification (brute force): " + countMoreOnes);
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |  ANSWER 2: " + totalMoreOnes + " sequences                  |");
        System.out.println("  +------------------------------------------+");
        System.out.println();
        
        // =====================================================================
        // PROBLEM 3: Exactly 3 zeros, no two adjacent
        // =====================================================================
        System.out.println("======================================================================");
        System.out.println("PROBLEM 3: How many sequences have EXACTLY 3 ZEROS with");
        System.out.println("           NO TWO ZEROS ADJACENT (separated by at least one 1)?");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  We have: 3 zeros and 7 ones");
        System.out.println("  Constraint: No two zeros can be next to each other");
        System.out.println();
        System.out.println("  SOLUTION using the 'stars and bars' / 'gaps' method:");
        System.out.println();
        System.out.println("  Step 1: First, place all 7 ones in a row:");
        System.out.println("          1 1 1 1 1 1 1");
        System.out.println();
        System.out.println("  Step 2: This creates 8 'gaps' where we can place zeros:");
        System.out.println("          _ 1 _ 1 _ 1 _ 1 _ 1 _ 1 _ 1 _");
        System.out.println("          ^   ^   ^   ^   ^   ^   ^   ^");
        System.out.println("          1   2   3   4   5   6   7   8  (8 gaps)");
        System.out.println();
        System.out.println("  Step 3: Choose 3 of these 8 gaps to place one zero each.");
        System.out.println("          Since each gap gets at most one zero, no two zeros");
        System.out.println("          will be adjacent!");
        System.out.println();
        System.out.println("  Formula: C(8, 3) = 8! / (3! * 5!)");
        System.out.printf("           C(8, 3) = %d%n", binomial(8, 3));
        System.out.println();
        
        System.out.println("  WHY 8 GAPS?");
        System.out.println("  ─────────────────────────────────────────────────────────────────");
        System.out.println("  With k ones, there are (k+1) possible positions for zeros:");
        System.out.println("    - 1 position before the first one");
        System.out.println("    - (k-1) positions between consecutive ones");
        System.out.println("    - 1 position after the last one");
        System.out.println("  Total: 1 + (k-1) + 1 = k + 1 = 7 + 1 = 8 gaps");
        System.out.println();
        
        // Verify by brute force
        int countNoAdjacent = 0;
        System.out.println("  All valid sequences (verification):");
        System.out.println("  ─────────────────────────────────────────────────────────────────");
        int printed = 0;
        for (int i = 0; i < (1 << n); i++) {
            String binary = toBinary(i, n);
            if (countZeros(binary) == 3 && noAdjacentZeros(binary)) {
                countNoAdjacent++;
                if (printed < 56) {  // Print all 56
                    System.out.printf("    %2d. %s%n", countNoAdjacent, binary);
                }
                printed++;
            }
        }
        System.out.println();
        System.out.println("  Verification (brute force): " + countNoAdjacent);
        System.out.println();
        System.out.println("  +------------------------------------------+");
        System.out.println("  |  ANSWER 3: " + binomial(8, 3) + " sequences                    |");
        System.out.println("  +------------------------------------------+");
        System.out.println();
        
        // =====================================================================
        // SUMMARY
        // =====================================================================
        System.out.println("======================================================================");
        System.out.println("                           SUMMARY");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  1. Sequences with equal zeros and ones (5 each):");
        System.out.printf("     C(10,5) = %d%n", binomial(10, 5));
        System.out.println();
        System.out.println("  2. Sequences with more ones than zeros:");
        System.out.printf("     C(10,0) + C(10,1) + C(10,2) + C(10,3) + C(10,4) = %d%n", totalMoreOnes);
        System.out.printf("     Or: (2^10 - C(10,5)) / 2 = %d%n", totalMoreOnes);
        System.out.println();
        System.out.println("  3. Sequences with exactly 3 zeros, no two adjacent:");
        System.out.printf("     C(8,3) = %d  (choosing 3 gaps from 8 available)%n", binomial(8, 3));
    }
}
