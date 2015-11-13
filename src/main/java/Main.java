import static spark.SparkBase.port;
import cardswithfriends.DBHandler;
import cardswithfriends.TemplateHandler;
import spark.Spark;


public class Main {

    public static void main(String[] args) {
    	Spark.staticFileLocation("/public");
        port(getHerokuAssignedPort());
        TemplateHandler.registerTemplates();
        //DBHandler.runTJsTestCode();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
    
}
