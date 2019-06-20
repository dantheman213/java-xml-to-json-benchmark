package app;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.nio.charset.Charset;
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
        var xmlRawPayload = readFile("/opt/payload.xml");

        System.out.println(String.format("Started at %s.", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date())));
        long startTime = System.currentTimeMillis();

        System.out.println(String.format("Starting %d threads for test...", threadCount));

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < threadCount; i++) {
            var thread = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < iterationCount; j++) {
                        convertXmlToJson(xmlRawPayload);
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

    private static void convertXmlToJson(String xml) {
        try {
            var json = XML.toJSONObject(xml);
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
    }

    private static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }
}
