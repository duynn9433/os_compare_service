import java.io.*;
import java.util.*;

public class ReadFile {
    public static void main(String[] args) throws Exception {
        String filename = args.length > 0 ? args[0] : "sample.csv";
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        List<String> lines = new ArrayList<>();

        long start = System.nanoTime();

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        long end = System.nanoTime();
        reader.close();

        System.out.printf("✅ Java read %d rows in %.4f seconds\n", lines.size(), (end - start) / 1e9);
    }
}