/**
 * Pizza Slices - Multiple Formulas and Approaches
 * 
 * Problem: Find the maximum number of pieces a circular pizza can be divided into
 * using n straight cuts, where:
 * - Every two cuts cross inside the pizza
 * - No three cuts meet at the same intersection point
 * 
 * This is known as the "Lazy Caterer's Sequence" or "Central Polygonal Numbers"
 */
public class Main {
    
    // METHOD 1: RECURRENCE RELATION
    public static int method1_Recurrence(int n) {
        if (n == 0) return 1;
        return method1_Recurrence(n - 1) + n;
    }
    
    // METHOD 2: CLOSED-FORM QUADRATIC FORMULA
    public static int method2_ClosedFormQuadratic(int n) {
        return (n * n + n + 2) / 2;
    }
    
    // METHOD 3: TRIANGULAR NUMBER FORMULA
    public static int method3_TriangularNumber(int n) {
        int triangular = n * (n + 1) / 2;
        return 1 + triangular;
    }
    
    // METHOD 4: BINOMIAL COEFFICIENTS
    public static int method4_BinomialCoefficients(int n) {
        int c0 = 1;
        int c1 = n;
        int c2 = n * (n - 1) / 2;
        return c0 + c1 + c2;
    }
    
    // Helper: Calculate binomial coefficient C(n,k)
    public static int binomial(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        int result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        return result;
    }
    
    // METHOD 5: EULER'S FORMULA
    public static int method5_EulerFormula(int n) {
        if (n == 0) return 1;
        int V = n * (n - 1) / 2 + 2 * n;
        int E = n * n + 2 * n;
        int F = 2 - V + E;
        return F - 1;
    }
    
    // METHOD 6: SUMMATION FORMULA
    public static int method6_Summation(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return 1 + sum;
    }
    
    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println("         PIZZA SLICES - MULTIPLE FORMULAS COMPARISON");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("All equivalent formulas for P(n) - max pizza pieces with n cuts:");
        System.out.println("  1. RECURRENCE:   P(0)=1, P(n)=P(n-1)+n");
        System.out.println("  2. QUADRATIC:    P(n) = (n^2 + n + 2) / 2");
        System.out.println("  3. TRIANGULAR:   P(n) = 1 + n(n+1)/2");
        System.out.println("  4. BINOMIAL:     P(n) = C(n,0) + C(n,1) + C(n,2)");
        System.out.println("  5. EULER'S:      Using V - E + F = 2");
        System.out.println("  6. SUMMATION:    P(n) = 1 + (1 + 2 + ... + n)");
        System.out.println();
        
        System.out.println("Verification - all methods give same results:");
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-4s | %-10s | %-10s | %-10s | %-10s%n", 
                          "n", "Recurrence", "Quadratic", "Triangular", "Binomial");
        System.out.println("----------------------------------------------------------------------");
        for (int n = 0; n <= 8; n++) {
            System.out.printf("%-4d | %-10d | %-10d | %-10d | %-10d%n",
                n, method1_Recurrence(n), method2_ClosedFormQuadratic(n),
                method3_TriangularNumber(n), method4_BinomialCoefficients(n));
        }
        System.out.println();
        
        System.out.println("======================================================================");
        System.out.println("  DETAILED DERIVATION: BINOMIAL FORMULA -> RECURRENCE RELATION");
        System.out.println("  Using Pascal's Identity: C(n,k) = C(n-1,k-1) + C(n-1,k)");
        System.out.println("======================================================================");
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 0: WHAT IS PASCAL'S IDENTITY?");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  Pascal's Identity: C(n,k) = C(n-1,k-1) + C(n-1,k)");
        System.out.println();
        System.out.println("  In Pascal's Triangle, each number is the sum of the two above it:");
        System.out.println();
        System.out.println("              1              Row 0: C(0,0)");
        System.out.println("            1   1            Row 1: C(1,0) C(1,1)");
        System.out.println("          1   2   1          Row 2: C(2,0) C(2,1) C(2,2)");
        System.out.println("        1   3   3   1        Row 3: C(3,0) C(3,1) C(3,2) C(3,3)");
        System.out.println("      1   4   6   4   1      Row 4: C(4,0) C(4,1) C(4,2) C(4,3) C(4,4)");
        System.out.println();
        System.out.println("  Example: C(4,2)=6 = C(3,1)+C(3,2) = 3+3 = 6");
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 1: START WITH THE BINOMIAL FORMULA");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  P(n) = C(n,0) + C(n,1) + C(n,2)");
        System.out.println();
        System.out.println("  Let's use n=4 as our example:");
        System.out.printf("  P(4) = C(4,0) + C(4,1) + C(4,2)%n");
        System.out.printf("  P(4) =   %d    +   %d    +   %d    = %d%n",
                          binomial(4,0), binomial(4,1), binomial(4,2),
                          binomial(4,0) + binomial(4,1) + binomial(4,2));
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 2: APPLY PASCAL'S IDENTITY TO EACH TERM");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  TERM 1: C(n,0)");
        System.out.println("  -----------------");
        System.out.println("  C(n,0) = 1 for any n (there's only 1 way to choose nothing)");
        System.out.println("  Also C(n-1,0) = 1");
        System.out.println("  So: C(n,0) = C(n-1,0) = 1");
        System.out.println();
        System.out.printf("  With n=4: C(4,0) = %d = C(3,0) = %d%n", binomial(4,0), binomial(3,0));
        System.out.println();
        
        System.out.println("  TERM 2: C(n,1) using Pascal's Identity");
        System.out.println("  -----------------");
        System.out.println("  Pascal's Identity: C(n,k) = C(n-1,k-1) + C(n-1,k)");
        System.out.println("  With k=1:          C(n,1) = C(n-1,0)   + C(n-1,1)");
        System.out.println();
        System.out.printf("  With n=4: C(4,1) = C(3,0) + C(3,1) = %d + %d = %d%n",
                          binomial(3,0), binomial(3,1), binomial(3,0) + binomial(3,1));
        System.out.println();
        
        System.out.println("  TERM 3: C(n,2) using Pascal's Identity");
        System.out.println("  -----------------");
        System.out.println("  Pascal's Identity: C(n,k) = C(n-1,k-1) + C(n-1,k)");
        System.out.println("  With k=2:          C(n,2) = C(n-1,1)   + C(n-1,2)");
        System.out.println();
        System.out.printf("  With n=4: C(4,2) = C(3,1) + C(3,2) = %d + %d = %d%n",
                          binomial(3,1), binomial(3,2), binomial(3,1) + binomial(3,2));
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 3: SUBSTITUTE ALL THREE EXPANDED TERMS");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  Original:");
        System.out.println("  P(n) = C(n,0)   +        C(n,1)         +        C(n,2)");
        System.out.println();
        System.out.println("  After substitution:");
        System.out.println("  P(n) = C(n-1,0) + [C(n-1,0) + C(n-1,1)] + [C(n-1,1) + C(n-1,2)]");
        System.out.println();
        System.out.println("  With n=4:");
        System.out.printf("  P(4) = C(3,0) + [C(3,0) + C(3,1)] + [C(3,1) + C(3,2)]%n");
        System.out.printf("  P(4) =   %d   + [  %d   +   %d  ] + [  %d   +   %d  ]%n",
                          binomial(3,0), binomial(3,0), binomial(3,1), binomial(3,1), binomial(3,2));
        System.out.printf("  P(4) =   %d   +       %d         +       %d%n",
                          binomial(3,0), binomial(3,0)+binomial(3,1), binomial(3,1)+binomial(3,2));
        System.out.printf("  P(4) = %d%n",
                          binomial(3,0) + (binomial(3,0)+binomial(3,1)) + (binomial(3,1)+binomial(3,2)));
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 4: REMOVE BRACKETS AND COUNT ALL TERMS");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  Remove the brackets:");
        System.out.println("  P(n) = C(n-1,0) + C(n-1,0) + C(n-1,1) + C(n-1,1) + C(n-1,2)");
        System.out.println("            ^          ^          ^          ^          ^");
        System.out.println("           1st        2nd        1st        2nd        only");
        System.out.println("          copy       copy       copy       copy       copy");
        System.out.println();
        System.out.println("  Count occurrences:");
        System.out.println("    * C(n-1,0) appears 2 times");
        System.out.println("    * C(n-1,1) appears 2 times");
        System.out.println("    * C(n-1,2) appears 1 time");
        System.out.println();
        System.out.println("  With n=4:");
        System.out.printf("  P(4) = %d + %d + %d + %d + %d  (five separate terms)%n",
                          binomial(3,0), binomial(3,0), binomial(3,1), binomial(3,1), binomial(3,2));
        System.out.printf("  P(4) = 1 + 1 + 3 + 3 + 3 = %d%n",
                          binomial(3,0)+binomial(3,0)+binomial(3,1)+binomial(3,1)+binomial(3,2));
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 5: REGROUP TO IDENTIFY P(n-1)");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  We have: C(n-1,0) + C(n-1,0) + C(n-1,1) + C(n-1,1) + C(n-1,2)");
        System.out.println();
        System.out.println("  Regroup cleverly into two parts:");
        System.out.println("  = [C(n-1,0) + C(n-1,1) + C(n-1,2)] + [C(n-1,0) + C(n-1,1)]");
        System.out.println("    \\_____________________________/");
        System.out.println("           This equals P(n-1)!");
        System.out.println();
        System.out.println("  Why? Because P(n-1) = C(n-1,0) + C(n-1,1) + C(n-1,2) by definition!");
        System.out.println();
        System.out.println("  So: P(n) = P(n-1) + [C(n-1,0) + C(n-1,1)]");
        System.out.println();
        System.out.println("  With n=4:");
        System.out.printf("  P(4) = [%d + %d + %d] + [%d + %d]%n",
                          binomial(3,0), binomial(3,1), binomial(3,2), binomial(3,0), binomial(3,1));
        System.out.printf("  P(4) = [  P(3)=%d  ] + [  %d  ]%n",
                          method4_BinomialCoefficients(3), binomial(3,0) + binomial(3,1));
        System.out.printf("  P(4) =      %d       +    %d%n",
                          method4_BinomialCoefficients(3), binomial(3,0) + binomial(3,1));
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 6: SIMPLIFY [C(n-1,0) + C(n-1,1)] = n");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  We need to simplify: C(n-1,0) + C(n-1,1)");
        System.out.println();
        System.out.println("  By definition of binomial coefficients:");
        System.out.println("    * C(n-1,0) = 1       (choosing 0 items from anything = 1 way)");
        System.out.println("    * C(n-1,1) = n-1     (choosing 1 item from n-1 items = n-1 ways)");
        System.out.println();
        System.out.println("  Therefore:");
        System.out.println("    C(n-1,0) + C(n-1,1) = 1 + (n-1) = n");
        System.out.println();
        System.out.println("  Verification for different values of n:");
        for (int n = 1; n <= 5; n++) {
            System.out.printf("    n=%d: C(%d,0) + C(%d,1) = %d + %d = %d = n%n",
                              n, n-1, n-1, binomial(n-1,0), binomial(n-1,1), binomial(n-1,0)+binomial(n-1,1));
        }
        System.out.println();
        
        System.out.println("----------------------------------------------------------------------");
        System.out.println("STEP 7: FINAL RESULT - THE RECURRENCE RELATION!");
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
        System.out.println("  Starting from:  P(n) = P(n-1) + [C(n-1,0) + C(n-1,1)]");
        System.out.println();
        System.out.println("  Substitute:     P(n) = P(n-1) + [   1     +   (n-1) ]");
        System.out.println();
        System.out.println("  Simplify:       P(n) = P(n-1) +          n");
        System.out.println();
        System.out.println("  +============================================================+");
        System.out.println("  |  RESULT: P(n) = P(n-1) + n   <-- The Recurrence Relation! |");
        System.out.println("  +============================================================+");
        System.out.println();
        
        System.out.println("  Final verification:");
        for (int n = 1; n <= 6; n++) {
            int pn_minus_1 = method4_BinomialCoefficients(n-1);
            int pn = method4_BinomialCoefficients(n);
            System.out.printf("    P(%d) = P(%d) + %d = %d + %d = %d%n", n, n-1, n, pn_minus_1, n, pn);
        }
        System.out.println();
        
        System.out.println("======================================================================");
        System.out.println("  SUMMARY: The combinatorial formula DERIVES the recurrence!");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("  Starting formula:   P(n) = C(n,0) + C(n,1) + C(n,2)");
        System.out.println("  Apply Pascal's Identity to expand each binomial coefficient");
        System.out.println("  Regroup to find P(n-1) and simplify the remaining terms");
        System.out.println("  Result:             P(n) = P(n-1) + n");
        System.out.println();
        System.out.println("  The sequence (Lazy Caterer's Sequence - OEIS A000124):");
        System.out.print("  ");
        for (int n = 0; n <= 12; n++) {
            System.out.print(method2_ClosedFormQuadratic(n));
            if (n < 12) System.out.print(", ");
        }
        System.out.println(", ...");
    }
}
