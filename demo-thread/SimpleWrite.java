import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

public class SimpleWrite {

    private static final String[] STATUSES = {"pending", "completed", "cancelled"};
    private static final int TOTAL_ORDERS = 10_000_000;

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        long totalDays = 365 * 5L;

        long startTime = System.currentTimeMillis();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.csv"), 1 << 16)) {
            writer.write("order_id,order_date,order_amount,order_status");
            writer.newLine();

            for (int i = 1; i <= TOTAL_ORDERS; i++) {
                LocalDate orderDate = startDate.plusDays(random.nextLong(totalDays));
                double amount = Math.round(random.nextDouble() * 9999_99) / 100.0;
                String status = STATUSES[random.nextInt(STATUSES.length)];

                writer.write(i + "," + orderDate + "," + amount + "," + status);
                writer.newLine();
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.printf("Done! Wrote %,d orders in %,d ms%n", TOTAL_ORDERS, elapsed);
    }
}

