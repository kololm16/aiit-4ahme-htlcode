package jsontutorial;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;

/**
 *
 * @author olive
 */
public class GsonBuilder {
    //Default constructor....
    //Gson gson = new Gson();
    
    //GsonBuilder....
    Gson gson = new com.google.gson.GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .serializeNulls()
            .create();
}
