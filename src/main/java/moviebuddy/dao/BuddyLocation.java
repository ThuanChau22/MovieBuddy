package moviebuddy.dao;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.errors.ApiException;

import java.util.List;
import java.io.IOException;

import moviebuddy.model.Theatre;
import moviebuddy.util.S;

public class BuddyLocation {

    // Obtain user's place id
    public static GeocodingResult getLocation(String zip) {
        GeocodingResult result = null;
        GeoApiContext context = null;
        try {
            context = new GeoApiContext.Builder().apiKey(S.ggKey()).build();
            GeocodingResult[] results = GeocodingApi.geocode(context, zip).await();
            if (results.length > 0) {
                result = results[0];
            }
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            context.shutdown();
        }
        return result;
    }

    public static Theatre getClosetTheatre(String origin) throws Exception {
        Theatre closestTheatre = null;
        GeoApiContext context = null;
        try {
            context = new GeoApiContext.Builder().apiKey(S.ggKey()).build();

            // Retrieve origin's place ids
            String[] origins = { "place_id:" + origin };

            // Retrieve theatre's place ids
            List<Theatre> theatres = new TheatreDAO().listTheatres();
            String[] destinations = new String[theatres.size()];
            int destinationIndex = 0;
            for (Theatre theatre : theatres) {
                GeocodingResult[] results = GeocodingApi.geocode(context, theatre.getZip()).await();
                if (results.length > 0) {
                    destinations[destinationIndex++] = "place_id:" + results[0].placeId;
                }
            }

            // Get closest location
            int closestTheatreIndex = 0;
            if (destinations.length > 0) {
                DistanceMatrix req = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations)
                        .mode(TravelMode.DRIVING).await();
                if (req.rows.length > 0) {
                    DistanceMatrixElement[] elements = req.rows[0].elements;
                    long closest = elements[closestTheatreIndex].distance.inMeters;
                    for (int i = 1; i < elements.length; i++) {
                        long distance = elements[i].distance.inMeters;
                        if (distance < closest) {
                            closest = distance;
                            closestTheatreIndex = i;
                        }
                    }
                }
            }
            closestTheatre = theatres.get(closestTheatreIndex);
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            context.shutdown();
        }
        return closestTheatre;
    }
}
