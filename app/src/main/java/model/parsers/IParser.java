package model.parsers;

/**
 * Created by walid on 27/01/2018.
 */
import org.json.JSONException;
import java.util.List;

public interface IParser<T> {
    String fromModel(T model) throws JSONException;
    String fromModel(List<T> model) throws JSONException;
    List<T> fromJson(String json) throws JSONException;
}