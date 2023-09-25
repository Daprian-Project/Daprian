package project.daprian.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLUtil {
    public static List<String> getContent(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the request method to GET
        conn.setRequestMethod("GET");

        // Get the response code
        int responseCode = conn.getResponseCode();

        // Check if the response code is 200 (OK)
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            List<String> contentLines = new ArrayList<>();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                contentLines.add(inputLine);
            }

            in.close();
            return contentLines;
        } else {
            throw new IOException("Failed to fetch content. Response code: " + responseCode);
        }
    }
}