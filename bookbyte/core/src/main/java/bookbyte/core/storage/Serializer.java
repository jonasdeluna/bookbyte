package bookbyte.core.storage;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * Interface for serializing and deserializing objects
 * @param <T> the class to serialize/deserialize
 */
public interface Serializer<T> extends JsonDeserializer<T>, JsonSerializer<T> {}
