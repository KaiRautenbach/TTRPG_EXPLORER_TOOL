package za.co.wethincode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiReader {

    public ApiReader(){

    }
    public String Reader()  {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.dnd5eapi.co/api/2014/monsters" ))
                    .GET() // Optional: GET is the default method
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }
}
