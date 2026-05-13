public class WriteFile {

    public static void main(String[] args) {
        String filePath = "customer_data.txt";
        // Write 100,000,000 customers (id, username, email, firstname, lastname)
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath))) {
            for (int i = 1; i <= 100_000_000; i++) {
                String line = String.format("%d,customer%d,customer%d@example.com,First%d,Last%d%n", i, i, i, i, i);
                writer.write(line);
            }
        } catch (java.io.IOException e) {
            System.err.println("An IOException occurred: " + e.getMessage());
        }
    }
    
}
