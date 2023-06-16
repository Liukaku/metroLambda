package dev.liukaku;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetStopById implements RequestHandler<Map<String, String>, String>, GetById {


    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        try {
            return getStopById(input.get("stop"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public String getStopById(String id) throws IOException {
        String urlString = String.format("https://api.tfgm.com/odata/Metrolinks(%s)", id);
        URL fetchUrl = new URL(urlString);
        HttpURLConnection goGet = (HttpURLConnection) fetchUrl.openConnection();
        goGet.setRequestMethod("GET");

        Map<String, String> headers = new HashMap<>();
        String secret = System.getenv("METRO_API_KEY");


        goGet.setRequestProperty("Ocp-Apim-Subscription-Key", secret);
        goGet.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        int responseCode = goGet.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(goGet.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);

        }
        in.close();
        goGet.disconnect();

        System.out.println(response);

        return response.toString();
    }
}