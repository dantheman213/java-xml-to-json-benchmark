package app;

import org.json.XML;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Application {
    private static int iterationCount = Integer.parseInt(System.getenv("ITERATION_COUNT"));
    private static int threadCount = Integer.parseInt(System.getenv("THREAD_COUNT"));

    public static void main(String[] args) throws Exception {
        System.out.println("Reading test XML payload from file...");
        var xmlRawPayload = new String(Files.readAllBytes(Paths.get("/opt/payload.xml")));

        System.out.println(String.format("Started at %s.", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date())));
        long startTime = System.currentTimeMillis();

        System.out.println(String.format("Starting %d threads for test...", threadCount));

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            var thread = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < iterationCount; j++) {
                        var json = XML.toJSONObject(xmlRawPayload);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        System.out.println(String.format("Each thread will run %d iterations for test...", iterationCount));
        System.out.println("Waiting for all threads to finish...");
        for (var thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("Completed in %d seconds.", ((endTime - startTime) / 1000)));
        System.out.println(String.format("Finished at %s.", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date())));
    }
}
