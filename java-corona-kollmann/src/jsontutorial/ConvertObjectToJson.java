package jsontutorial;

import com.google.gson.Gson;
import java.util.Arrays;

/**
 *
 * @author olive
 */
public class ConvertObjectToJson {
    
    public static void main(String[] args) {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstName("Hans");
        employee.setLastName("Huber");
        employee.setRoles(Arrays.asList("ADMIN", "MANAGER"));
        
        Gson gson = new Gson();
        
        System.out.println(gson.toJson(employee));
    }
}
