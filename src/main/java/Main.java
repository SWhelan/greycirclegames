import greycirclegames.DatabaseConnector;
import greycirclegames.frontend.TemplateHandler;
import spark.Spark;

public class Main {

    public static void main(String[] args) {
    	Spark.staticFileLocation("/static");
        Spark.port(getHerokuAssignedPort());
        TemplateHandler.registerTemplates();
        DatabaseConnector.setDefaultDatabase();
        DevHandler.addDefaultUsers();
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
    
}
