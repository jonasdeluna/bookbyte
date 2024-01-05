package bookbyte.core.storage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Model access for direct access to the model.
 * Saving and loading is only in memory.
 *
 * @param <T> the class to serialize/deserialize
 */
public class DirectModelAccess<T> implements ModelAccess<T> {


    private final Collection<T> data = new ArrayList<>();

    @Override
    public Collection<T> read() {
        return data;
    }

    @Override
    public void save(Collection<T> data) {
        this.data.clear();
        this.data.addAll(data);
    }
}
