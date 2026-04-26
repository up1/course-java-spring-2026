import java.io.*;
import java.util.HashMap;

public class ReadOrderById {

    private static final String FILE = "orders.csv";

    // ----------------------------------------------------------------
    // Approach 1 : O(n) linear scan
    // Reads through the entire file every time until the id is found.
    // Time : O(n) — worst case reads all 10 M lines
    // Memory: O(1) — no data stored in memory
    // ----------------------------------------------------------------
    static double findByIdLinear(int targetId) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE), 1 << 20)) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                int comma = line.indexOf(',');
                int id = Integer.parseInt(line.substring(0, comma));
                if (id == targetId) {
                    // order_id,order_date,order_amount,order_status
                    int c2 = line.indexOf(',', comma + 1);
                    int c3 = line.indexOf(',', c2 + 1);
                    return Double.parseDouble(line.substring(c2 + 1, c3));
                }
            }
        }
        return -1; // not found
    }

    // ----------------------------------------------------------------
    // Approach 2 : O(1) HashMap lookup (after O(n) build phase)
    // Loads order_id -> order_amount into a HashMap once, then any
    // subsequent lookup is O(1).
    // Time : O(n) build + O(1) per query
    // Memory: O(n) — holds all 10 M entries
    // ----------------------------------------------------------------
    static HashMap<Integer, Double> buildIndex() throws IOException {
        HashMap<Integer, Double> index = new HashMap<>(13_000_000, 0.75f);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE), 1 << 20)) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                // order_id,order_date,order_amount,order_status
                int c1 = line.indexOf(',');
                int c2 = line.indexOf(',', c1 + 1);
                int c3 = line.indexOf(',', c2 + 1);
                int id = Integer.parseInt(line.substring(0, c1));
                double amount = Double.parseDouble(line.substring(c2 + 1, c3));
                index.put(id, amount);
            }
        }
        return index;
    }

    // ----------------------------------------------------------------
    // Demo: compare both approaches for a few sample IDs
    // ----------------------------------------------------------------
    public static void main(String[] args) throws Exception {
        int[] sampleIds = { 1, 500_000, 5_000_000, 9_999_999 };

        System.out.println("=== Approach 1: O(n) linear scan ===");
        for (int id : sampleIds) {
            long t0 = System.currentTimeMillis();
            double amount = findByIdLinear(id);
            long elapsed = System.currentTimeMillis() - t0;
            System.out.printf("  order_id=%,10d  amount=%10.2f  time=%d ms%n", id, amount, elapsed);
        }

        System.out.println();
        System.out.println("=== Approach 2: O(1) HashMap (building index…) ===");
        long t0 = System.currentTimeMillis();
        HashMap<Integer, Double> index = buildIndex();
        long buildTime = System.currentTimeMillis() - t0;
        System.out.printf("  Index built: %,d entries in %d ms%n", index.size(), buildTime);

        for (int id : sampleIds) {
            long t1 = System.currentTimeMillis();
            Double amount = index.get(id);
            long elapsed = System.currentTimeMillis() - t1;
            System.out.printf("  order_id=%,10d  amount=%10.2f  time=%d ms%n",
                    id, amount != null ? amount : -1.0, elapsed);
        }
    }
}
