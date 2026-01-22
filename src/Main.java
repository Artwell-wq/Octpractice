/**
 * Pizza Slices - Multiple Formulas and Approaches
 * 
 * Problem: Find the maximum number of pieces a circular pizza can be divided into
 * using n straight cuts, where:
 * - Every two cuts cross inside the pizza
 * - No three cuts meet at the same intersection point
 * 
 * This is known as the "Lazy Caterer's Sequence" or "Central Polygonal Numbers"
 * 
 * =========================================================================
 * MULTIPLE EQUIVALENT FORMULAS TO SOLVE THIS PROBLEM:
 * =========================================================================
 * 
 * METHOD 1: RECURRENCE RELATION
 * -----------------------------
 * P(0) = 1
 * P(n) = P(n-1) + n
 * 
 * METHOD 2: CLOSED-FORM (Quadratic)
 * ---------------------------------
 * P(n) = (n² + n + 2) / 2
 * 
 * METHOD 3: TRIANGULAR NUMBER FORMULA
 * ------------------------------------
 * P(n) = 1 + T(n)  where T(n) = n(n+1)/2 is the nth triangular number
 * P(n) = 1 + n(n+1)/2
 * 
 * METHOD 4: BINOMIAL COEFFICIENTS (Combinatorial)
 * ------------------------------------------------
 * P(n) = C(n,0) + C(n,1) + C(n,2)
 * P(n) = 1 + n + n(n-1)/2
 * 
 * This elegant formula comes from:
 *   - C(n,0) = 1      : the original region
 *   - C(n,1) = n      : each line adds at least 1 region  
 *   - C(n,2) = n(n-1)/2 : each pair of lines creates an intersection,
 *                         and each intersection adds 1 more region
 * 
 * METHOD 5: EULER'S FORMULA FOR PLANAR GRAPHS
 * -------------------------------------------
 * Using V - E + F = 2 (Euler's formula)
 * Where: V = vertices, E = edges, F = faces (including outer face)
 * 
 * For n lines:
 *   - Intersection points (inside): C(n,2) = n(n-1)/2
 *   - Points on circle boundary: 2n
 *   - Total vertices V = n(n-1)/2 + 2n
 *   - Edges E = n(n-1)/2 * 2 + 2n + 2n = n² + 2n  
 *   - Faces F = 2 - V + E = P(n) + 1 (outer face)
 *   - Therefore P(n) = (n² + n + 2) / 2
 * 
 * METHOD 6: SUMMATION FORMULA
 * ---------------------------
 * P(n) = 1 + Σ(i=1 to n) i = 1 + 1 + 2 + 3 + ... + n
 */
public class Main {
    
    // =========================================================================
    // METHOD 1: RECURRENCE RELATION (Recursive)
    // =========================================================================
    /**
     * P(0) = 1
     * P(n) = P(n-1) + n
     */
    public static int method1_Recurrence(int n) {
        if (n == 0) return 1;
        return method1_Recurrence(n - 1) + n;
    }
    
    // =========================================================================
    // METHOD 2: CLOSED-FORM QUADRATIC FORMULA
    // =========================================================================
    /**
     * P(n) = (n² + n + 2) / 2
     */
    public static int method2_ClosedFormQuadratic(int n) {
        return (n * n + n + 2) / 2;
    }
    
    // =========================================================================
    // METHOD 3: TRIANGULAR NUMBER FORMULA
    // =========================================================================
    /**
     * P(n) = 1 + T(n) where T(n) = n(n+1)/2
     */
    public static int method3_TriangularNumber(int n) {
        int triangular = n * (n + 1) / 2;  // T(n) = nth triangular number
        return 1 + triangular;
    }
    
    // =========================================================================
    // METHOD 4: BINOMIAL COEFFICIENTS (Combinatorial)
    // =========================================================================
    /**
     * P(n) = C(n,0) + C(n,1) + C(n,2)
     * 
     * This is the sum of the first 3 entries in row n of Pascal's Triangle!
     */
    public static int method4_BinomialCoefficients(int n) {
        int c0 = 1;                      // C(n,0) = 1
        int c1 = n;                      // C(n,1) = n
        int c2 = n * (n - 1) / 2;        // C(n,2) = n(n-1)/2
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
    
    // Alternative using binomial function
    public static int method4_BinomialAlt(int n) {
        return binomial(n, 0) + binomial(n, 1) + binomial(n, 2);
    }
    
    // =========================================================================
    // METHOD 5: EULER'S FORMULA (V - E + F = 2)
    // =========================================================================
    /**
     * Using Euler's formula for planar graphs: V - E + F = 2
     * Solving for F (faces) gives us regions + 1 (for outer face)
     * 
     * For n lines in a circle:
     * - Vertices V = C(n,2) + 2n  (intersections inside + points on boundary)
     * - Edges E = C(n,2)*2 + n + 2n = n² + 2n - n(n-1)/2 + n(n-1)/2 + n... 
     * 
     * Actually simpler: Just use closed form since all formulas are equivalent!
     * The Euler approach is more of a theoretical derivation method.
     */
    public static int method5_EulerFormula(int n) {
        if (n == 0) return 1;
        
        // Using Euler's formula V - E + F = 2:
        // V (vertices) = C(n,2) + 2n = n(n-1)/2 + 2n (intersections + boundary points)
        // E (edges) = Each intersection creates 4 edge endpoints, boundary has 2n edges
        //           = n(n-1) + 2n + n = n² + 2n (this counts line segments + circle arcs)
        // Actually, a cleaner counting:
        // - Each of n lines is cut into (n-1)+1 = n segments by other lines, but only
        //   the part inside matters. With boundary, each line has n-1 intersections + 2 boundary = n+1 segments
        // - Circle boundary is cut into 2n arcs
        // Total edges E = n*(n+1)/2 *2 + ... this gets complex
        
        // The theoretical result from Euler's formula gives:
        // F = 2 - V + E = 1 + n + C(n,2) = 1 + n + n(n-1)/2
        // Which equals our closed form (n² + n + 2)/2
        
        // For demonstration, we compute using the derived result:
        int V = n * (n - 1) / 2 + 2 * n;  // intersections + boundary points
        int E = n * n + 2 * n;             // line segments + boundary arcs
        int F = 2 - V + E;                 // Euler's formula
        return F - 1;  // Subtract 1 for the outer/infinite face
    }
    
    // =========================================================================
    // METHOD 6: SUMMATION FORMULA (Iterative)
    // =========================================================================
    /**
     * P(n) = 1 + Σ(i=1 to n) i
     */
    public static int method6_Summation(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return 1 + sum;
    }
    
    // =========================================================================
    // MAIN - Demonstrate all methods
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║         PIZZA SLICES - MULTIPLE FORMULAS COMPARISON                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("┌─────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  ALL EQUIVALENT FORMULAS FOR P(n) - Maximum pizza pieces with n cuts│");
        System.out.println("├─────────────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. RECURRENCE:     P(0)=1, P(n)=P(n-1)+n                           │");
        System.out.println("│  2. QUADRATIC:      P(n) = (n² + n + 2) / 2                         │");
        System.out.println("│  3. TRIANGULAR:     P(n) = 1 + n(n+1)/2                             │");
        System.out.println("│  4. BINOMIAL:       P(n) = C(n,0) + C(n,1) + C(n,2)                 │");
        System.out.println("│  5. EULER'S:        Using V - E + F = 2                             │");
        System.out.println("│  6. SUMMATION:      P(n) = 1 + (1 + 2 + 3 + ... + n)                │");
        System.out.println("└─────────────────────────────────────────────────────────────────────┘");
        System.out.println();
        
        System.out.println("VERIFICATION: All methods produce identical results:");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-6s │ %-10s │ %-10s │ %-10s │ %-10s │ %-8s │ %-10s%n", 
                          "n", "Recurrence", "Quadratic", "Triangular", "Binomial", "Euler", "Summation");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        
        for (int n = 0; n <= 10; n++) {
            System.out.printf("%-6d │ %-10d │ %-10d │ %-10d │ %-10d │ %-8d │ %-10d%n",
                n,
                method1_Recurrence(n),
                method2_ClosedFormQuadratic(n),
                method3_TriangularNumber(n),
                method4_BinomialCoefficients(n),
                method5_EulerFormula(n),
                method6_Summation(n)
            );
        }
        
        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    FORMULA DERIVATIONS                                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        System.out.println("WHY THE BINOMIAL FORMULA C(n,0) + C(n,1) + C(n,2) WORKS:");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        System.out.println("  • C(n,0) = 1       → The original uncut pizza (1 region)");
        System.out.println("  • C(n,1) = n       → Each of n lines adds at least 1 new region");
        System.out.println("  • C(n,2) = n(n-1)/2 → Each PAIR of lines creates 1 intersection,");
        System.out.println("                        and each intersection adds 1 more region");
        System.out.println();
        System.out.println("  This is equivalent to summing the first 3 entries of Pascal's Triangle row n!");
        System.out.println();
        
        System.out.println("PASCAL'S TRIANGLE CONNECTION:");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        for (int n = 0; n <= 6; n++) {
            System.out.printf("  Row %d: C(%d,0)=%d + C(%d,1)=%d + C(%d,2)=%d = %d pieces%n",
                n, n, binomial(n,0), n, binomial(n,1), n, binomial(n,2),
                binomial(n,0) + binomial(n,1) + binomial(n,2));
        }
        System.out.println();
        
        System.out.println("THE SEQUENCE (Lazy Caterer's Sequence - OEIS A000124):");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        System.out.print("  ");
        for (int n = 0; n <= 15; n++) {
            System.out.print(method2_ClosedFormQuadratic(n));
            if (n < 15) System.out.print(", ");
        }
        System.out.println(", ...");
    }
}