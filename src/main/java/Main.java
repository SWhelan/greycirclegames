import static spark.Spark.get;
import static spark.SparkBase.port;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/hello", (req, res) -> {return "<h1>Hello World</h1>";});
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

}
