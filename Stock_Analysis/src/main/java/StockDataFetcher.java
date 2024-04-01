import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Fetches stock data from an online API service.
 * This class is responsible for retrieving daily time series data for a given stock symbol
 * using the Alpha Vantage API.
 */
public class StockDataFetcher {

    private final String apiKey = "QPFV3HKS6884L9KC";

    /**
     * Fetches the daily time series data for a specific stock symbol.
     *
     * @param symbol The stock symbol for which to fetch the time series data.
     * @return A {@link JsonObject} containing the time series data. Returns
     * {@code null} in case of an error.
     */
    public JsonObject fetchStockData(String symbol) {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
                + symbol + "&apikey=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Send the request and receive the response as a String.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                    .ofString());
            // Parse the response body to a JsonObject and return.
            return JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
