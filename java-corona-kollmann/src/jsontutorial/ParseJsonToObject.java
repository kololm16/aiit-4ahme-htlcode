package jsontutorial;

import com.google.gson.Gson;

/**
 *
 * @author olive
 */
public class ParseJsonToObject {
    
    public static void main(String[] args) {
        Gson gson = new Gson();
        
        System.out.println(
            gson.fromJson("{'id':1,'firstName':'Hans','lastName':'Huber','roles':['ADMIN','MANAGER']}", Employee.class));  
    }
}
