package com.londonusers;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class UsersInLondon
{

    private static final String USERS_ENDPOINT = "https://bpdts-test-app.herokuapp.com";
    private static final String USERS_ENDPOINT_PATH = "/city/London/users";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    
    public static void main(String[] args) throws IOException, InterruptedException
    {
        // 1. Create a client
        HttpClient client = HttpClient.newHttpClient();

        // 2. Create the request
        HttpRequest request = HttpRequest.newBuilder()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .uri(URI.create(USERS_ENDPOINT + USERS_ENDPOINT_PATH))
                //.GET(HttpRequest.BodyPublishers.ofString()) // POST
                .build();

        // 3. Send the request and receive response
         //HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                 .thenApply(HttpResponse::body)
                 .thenApply(UsersInLondon::parse)
                 .join();

    }


       // Create a list of users who qualify as living in London or within
       // a 50-mile radius of London
    public static List<User> parse(String responseBody)
    {
        List<User> qualifyingUsers = new ArrayList();

        // London Latitude and Longitude coordinates converted into degrees
        // 51 deg 30 min 26 sec N
        double londonLat = 51 + (30 / 60.0) + (26 / 60.0 / 60.0);

        // 0 deg -7 min -39 sec W
        double londonLon = 0 + (-7 / 60.0) + (-39 / 60.0 / 60.0);

        JSONArray usersLondon = new JSONArray((responseBody));

        for (int i = 0; i < usersLondon.length(); i++)
        {
            JSONObject userLondon = usersLondon.getJSONObject(i);

            double latitude = userLondon.getDouble("latitude");
            double longitude = userLondon.getDouble("longitude");

            double userLat = latitude;
            double userLon = longitude;

            GeodesicData result =
                    Geodesic.WGS84.Inverse(londonLat, londonLon, userLat, userLon);

            double distanceInMeters = result.s12;
            double distanceInMiles = distanceInMeters / 1609.34;


            if (distanceInMiles <= 50.00)
            {
                User user = new User();
                System.out.println("People who live in London");

                for (int x = 0; x < usersLondon.length(); x++)
                {
                    System.out.println("id " + userLondon.getInt("id"));
                    System.out.println("First Name: " + userLondon.getString("first_name"));
                    System.out.println("Last Name: " + userLondon.getString("last_name"));
                    System.out.println("Email: " + userLondon.getString("email"));
                    System.out.println("ip_address: " + userLondon.getString("ip_address"));
                    System.out.println("Latitude: " + userLondon.getDouble("latitude"));
                    System.out.println("Longitude: " + userLondon.getDouble("longitude"));
                    System.out.println("-------------------------------------------------------");
                    qualifyingUsers.add(user);
                }

            }

        }
        System.out.println(responseBody);
        return qualifyingUsers;


    }
}

