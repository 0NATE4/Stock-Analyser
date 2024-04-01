import java.util.Scanner;
import com.google.gson.JsonObject;


/**
 * The main entry point for the Stock Analysis application.
 * This class handles user input to fetch and analyse stock data using specified indicators.
 */
public class Main {

    /**
     * The main method that initiates the program, allowing users to input stock symbols
     * for analysis and displaying the analysis results.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter the stock symbol you wish to analyse (or type 'exit' to " +
                    "quit): ");
            String symbol = scanner.nextLine(); // Read user input

            if (symbol.equalsIgnoreCase("exit")) {
                break; // Exit the loop if user types 'exit'
            }

            StockDataFetcher fetcher = new StockDataFetcher();
            JsonObject data = fetcher.fetchStockData(symbol); // Fetch the stock data.

            if (data != null) {
                // If data fetching is successful, analyse the trend with the StockTrendAnalyser.
                StockTrendAnalyser analyser = new StockTrendAnalyser();
                analyser.analyseTrend(data);
            } else {
                System.out.println("Failed to fetch stock data for symbol: " + symbol);
            }
            System.out.println(); // Print an empty line for better readability
        }
        System.out.println("Exiting program.");
        scanner.close(); 
    }
}
