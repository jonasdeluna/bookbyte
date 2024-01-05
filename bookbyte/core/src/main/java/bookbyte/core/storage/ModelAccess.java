package bookbyte.core.storage;

import java.util.Collection;

/**
 * Interface for reading and writing to a data source
 *
 * @param <T> the class to deserialize
 */
public interface ModelAccess<T> {

    /**
     * Reads the data from the data source
     * @return the data
     */
    Collection<T> read();

    /**
     * Saves the data to the data source
     * @param data the data to save
     */
    void save(Collection<T> data);
}
