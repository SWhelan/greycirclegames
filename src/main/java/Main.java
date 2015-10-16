import static spark.SparkBase.port;

import templates.TemplateHandler;


public class Main {

    public static void main(String[] args) {
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
