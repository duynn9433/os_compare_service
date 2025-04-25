import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WriteFile {
    public static void main(String[] args) throws Exception {
        int lines = args.length > 0 ? Integer.parseInt(args[0]) : 10000;
        String file = args.length > 1 ? args[1] : "output.csv";

        long start = System.currentTimeMillis();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
        for (int i = 1; i <= lines; i++) {
            writer.write("Line " + i + ",Some data here\n");
        }
        writer.close();
        long end = System.currentTimeMillis();

        System.out.printf("[Java] Wrote %d lines to %s in %.4f seconds.%n", lines, file, (end - start) / 1000.0);
    }
}