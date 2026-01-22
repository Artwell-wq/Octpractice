/**
 * Pizza Slices Recurrence Relation
 * 
 * Problem: Find the maximum number of pieces a circular pizza can be divided into
 * using n straight cuts, where:
 * - Every two cuts cross inside the pizza
 * - No three cuts meet at the same intersection point
 * 
 * This is known as the "Lazy Caterer's Sequence" or "Central Polygonal Numbers"
 * 
 * RECURRENCE RELATION:
 * ====================
 * P(0) = 1                    (base case: no cuts = 1 piece)
 * P(n) = P(n-1) + n           (recurrence: nth cut adds n new pieces)
 * 
 * DERIVATION:
 * ===========
 * When we add the nth cut:
 * - It can intersect at most (n-1) previous cuts inside the pizza
 * - Each intersection divides the new cut into segments
 * - The nth line crosses n-1 existing lines, creating n segments inside the pizza
 * - Each segment divides an existing region into two, adding 1 piece per segment
 * - But the line itself starts by splitting 1 region, then each intersection adds 1 more
 * - Total new pieces from nth cut = 1 + (n-1) = n
 * 
 * Therefore: P(n) = P(n-1) + n
 * 
 * CLOSED-FORM FORMULA:
 * ====================
 * P(n) = 1 + n(n+1)/2 = (n² + n + 2)/2
 * 
 * This is derived by expanding the recurrence:
 * P(n) = P(0) + 1 + 2 + 3 + ... + n = 1 + n(n+1)/2
 */
public class Main {
    
    /**
     * Calculates pizza slices using the recurrence relation (recursive approach)
     * P(0) = 1
     * P(n) = P(n-1) + n
     */
    public static int pizzaSlicesRecursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of cuts cannot be negative");
        }
        if (n == 0) {
            return 1;  // Base case: 0 cuts = 1 piece
        }
        return pizzaSlicesRecursive(n - 1) + n;  // Recurrence: P(n) = P(n-1) + n
    }
    
    /**
     * Calculates pizza slices using the recurrence relation (iterative approach)
     * P(n) = P(n-1) + n, starting from P(0) = 1
     */
    public static int pizzaSlicesIterative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of cuts cannot be negative");
        }
        int pieces = 1;  // P(0) = 1
        for (int i = 1; i <= n; i++) {
            pieces += i;  // P(i) = P(i-1) + i
        }
        return pieces;
    }
    
    /**
     * Calculates pizza slices using the closed-form formula
     * P(n) = 1 + n(n+1)/2 = (n² + n + 2)/2
     */
    public static int pizzaSlicesClosedForm(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of cuts cannot be negative");
        }
        return (n * n + n + 2) / 2;
    }
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    PIZZA SLICES RECURRENCE RELATION");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("RECURRENCE RELATION:");
        System.out.println("  P(0) = 1           (base case)");
        System.out.println("  P(n) = P(n-1) + n  (recurrence)");
        System.out.println();
        System.out.println("CLOSED-FORM FORMULA:");
        System.out.println("  P(n) = (n² + n + 2) / 2");
        System.out.println();
        System.out.println("=================================================");
        System.out.println("   VERIFICATION TABLE");
        System.out.println("=================================================");
        System.out.println();
        System.out.printf("%-8s %-12s %-12s %-12s%n", "Cuts(n)", "Recursive", "Iterative", "Closed-Form");
        System.out.println("-------------------------------------------------");
        
        for (int n = 0; n <= 10; n++) {
            int recursive = pizzaSlicesRecursive(n);
            int iterative = pizzaSlicesIterative(n);
            int closedForm = pizzaSlicesClosedForm(n);
            System.out.printf("%-8d %-12d %-12d %-12d%n", n, recursive, iterative, closedForm);
        }
        
        System.out.println();
        System.out.println("=================================================");
        System.out.println("   STEP-BY-STEP EXPLANATION");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("n=0: No cuts → 1 piece (the whole pizza)");
        System.out.println("n=1: 1st cut divides pizza → 1+1 = 2 pieces");
        System.out.println("n=2: 2nd cut crosses 1st cut → 2+2 = 4 pieces");
        System.out.println("n=3: 3rd cut crosses 2 cuts → 4+3 = 7 pieces");
        System.out.println("n=4: 4th cut crosses 3 cuts → 7+4 = 11 pieces");
        System.out.println("...");
        System.out.println("n=k: kth cut crosses k-1 cuts → adds k pieces");
        System.out.println();
        System.out.println("The sequence: 1, 2, 4, 7, 11, 16, 22, 29, 37, 46, 56, ...");
        System.out.println("(Also known as the Lazy Caterer's Sequence - OEIS A000124)");
    }
}