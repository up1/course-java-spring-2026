
import java.util.BitSet;

public class CheckData {

    private static final double FALSE_POSITIVE_RATE = 0.01;

    public static void main(String[] args) {
        String filePath = "customer_data.txt";
        // Read data from file customers (id, username, email, firstname, lastname)
        // Create Bloom filter to check username is existing or not ?
        try {
            long recordCount = countRecords(filePath);
            SimpleBloomFilter usernameFilter = SimpleBloomFilter.createFor(recordCount, FALSE_POSITIVE_RATE);

            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String username = parts[1];
                        // Create Bloom filter for checking username
                        createBloomFilter(usernameFilter, username);

                    }
                }
            }

            String[] usernamesToCheck = args.length > 0
                    ? args
                    : new String[] { "customer1", "customer500", "missing-user" };

            // Check if the usernames might exist in the Bloom filter
            for (String usernameToCheck : usernamesToCheck) {
                boolean mightExist = usernameFilter.mightContain(usernameToCheck);
                System.out.printf("Username '%s' %s%n",
                        usernameToCheck,
                        mightExist ? "might exist" : "does not exist");
            }
        } catch (java.io.IOException e) {
            System.err.println("An IOException occurred: " + e.getMessage());
        }
    }

    private static long countRecords(String filePath) throws java.io.IOException {
        long count = 0;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
        }

        return Math.max(count, 1);
    }

    private static void createBloomFilter(SimpleBloomFilter bloomFilter, String username) {
        bloomFilter.add(username);
    }

    private static final class SimpleBloomFilter {
        private final BitSet bits;
        private final int size;
        private final int hashFunctionCount;

        private SimpleBloomFilter(int size, int hashFunctionCount) {
            this.size = size;
            this.hashFunctionCount = hashFunctionCount;
            this.bits = new BitSet(size);
        }

        private static SimpleBloomFilter createFor(long expectedInsertions, double falsePositiveRate) {
            double ln2 = Math.log(2);
            int size = (int) Math.ceil(-(expectedInsertions * Math.log(falsePositiveRate)) / (ln2 * ln2));
            int hashFunctionCount = Math.max(1, (int) Math.round((size / (double) expectedInsertions) * ln2));
            return new SimpleBloomFilter(size, hashFunctionCount);
        }

        private void add(String value) {
            for (int index : getIndexes(value)) {
                bits.set(index);
            }
        }

        private boolean mightContain(String value) {
            for (int index : getIndexes(value)) {
                if (!bits.get(index)) {
                    return false;
                }
            }
            return true;
        }

        private int[] getIndexes(String value) {
            int[] indexes = new int[hashFunctionCount];
            int hash1 = value.hashCode();
            int hash2 = Integer.rotateLeft(hash1, 16) ^ 0x5bd1e995;

            for (int i = 0; i < hashFunctionCount; i++) {
                int combinedHash = hash1 + i * hash2;
                indexes[i] = Math.floorMod(combinedHash, size);
            }

            return indexes;
        }
    }
}
