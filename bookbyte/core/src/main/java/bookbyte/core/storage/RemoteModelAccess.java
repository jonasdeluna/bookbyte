package bookbyte.core.storage;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for reading and writing to a remote server
 *
 * @param <T> the class to deserialize
 */
public class RemoteModelAccess<T> implements ModelAccess<T> {


    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private final URI writeUri;
    private final URI readUri;
    private final Serializer<T> serializer;
    private final Class<T> type;


    /**
     * Constructor for RemoteModelAccess.
     * @param writeUri the URI to write to
     * @param readUri the URI to read from
     * @param serializer the serializer to use
     * @param type the type of the class to deserialize
     */
    public RemoteModelAccess(URI writeUri, URI readUri, Serializer<T> serializer, Class<T> type) {
        this.writeUri = writeUri;
        this.readUri = readUri;
        this.serializer = serializer;
        this.type = type;
    }


    @Override
    public Collection<T> read() {

        List<T> data = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder(readUri)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .GET()
                .build();


        try {
            final HttpResponse<String> response =
                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

            final String jsonString = response.body();


            JsonElement jsonElement = JsonParser.parseString(jsonString);
            JsonArray jsonArray = new JsonArray();
            if (jsonElement.isJsonArray()) {
                jsonArray = jsonElement.getAsJsonArray();
            }

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(type, serializer);

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i).isJsonNull()) {
                    continue;
                }
                T t = gsonBuilder.create().fromJson(jsonArray.get(i), type);
                data.add(t);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    @Override
    public void save(Collection<T> data) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, serializer);

        String json = gsonBuilder.create().toJson(data);

        HttpRequest request = HttpRequest.newBuilder(writeUri)
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method for checking if a server is running.
     * @param uri the URI of the server
     * @return true if the server is running, false otherwise
     */
    public static boolean isServerRunning(URI uri) {

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header(ACCEPT_HEADER, APPLICATION_JSON)
                .GET()
                .build();

        try {
            final HttpResponse<String> response =
                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() >= 200 && response.statusCode() < 300;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
