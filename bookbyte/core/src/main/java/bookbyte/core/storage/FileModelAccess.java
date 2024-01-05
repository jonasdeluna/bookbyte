package bookbyte.core.storage;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for reading and writing to a file
 * 
 * @param <T> the class to deserialize
 */
public class FileModelAccess<T> implements ModelAccess<T> {

    private final Serializer<T> serializer;
    private final File file;
    private final Class<T> type;

    /**
     * Constructor for FileAccessModel
     *
     * @param serializer the serializer to use
     * @param file         the file to load
     * @param type         the type of the class to deserialize
     */
    public FileModelAccess(Serializer<T> serializer, File file, Class<T> type) {
        this.serializer = serializer;
        this.file = file;
        this.type = type;
    }


    @Override
    public Collection<T> read() {

        if (!file.exists()) {
            return new ArrayList<>();
        }

        List<T> data = new ArrayList<>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, serializer);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {


            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray jsonArray = new JsonArray();
            if (jsonElement.isJsonArray()) {
                jsonArray = jsonElement.getAsJsonArray();
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i).isJsonNull()) {
                    continue;
                }
                T t = gsonBuilder.create().fromJson(jsonArray.get(i), type);
                data.add(t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void save(Collection<T> data) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write("[");
            boolean isFirst = true;
            for (T d : data) {
                if (!isFirst) {
                    writer.write(",");
                }
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(d.getClass(), this.serializer);
                Gson gson = gsonBuilder.create();

                String json = gson.toJson(d);
                writer.write(json);

                isFirst = false;
            }
            writer.write("]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
