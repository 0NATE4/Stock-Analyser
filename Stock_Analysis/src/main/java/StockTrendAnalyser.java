import com.google.gson.JsonObject;
import StockAnalysisIndicators.SMAIndicator;
import StockAnalysisIndicators.RSIIndicator;
import StockAnalysisIndicators.MACDIndicator;
import java.util.*;

/**
 * Analyses stock trends based on historical data using various technical indicators
 * such as SMA (Simple Moving Average), RSI (Relative Strength Index), and MACD
 * (Moving Average Convergence Divergence).
 */
public class StockTrendAnalyser {

    /**
     * Analyses stock trends from provided JSON data containing historical stock prices.
     *
     * @param data The JSON object containing the stock's historical data.
     */
    public void analyseTrend(JsonObject data) {
        try {
            JsonObject timeSeries = timeSeriesData(data);
            // Ensure there is data to analyse
            if (timeSeries == null) return;

            List<String> dates = new ArrayList<>(timeSeries.keySet());
            Collections.sort(dates); // Ensure the dates are sorted

            List<Double> closingPrices = getClosingPrices(dates, timeSeries);

            printSMAAnalysis(closingPrices);
            printRSIAnalysis(closingPrices);
            printMACDAnalysis(closingPrices);

        } catch (Exception e) {
            System.err.println("Error analysing trend: " + e.getMessage());
        }
    }

    /**
     * Extracts the "Time Series (Daily)" data from the provided JSON data.
     *
     * @param data The JSON object containing the stock's historical data.
     * @return A JSON object representing the "Time Series (Daily)" data.
     */
    private JsonObject timeSeriesData(JsonObject data) {
        return data.getAsJsonObject("Time Series (Daily)");
    }

    /**
     * Retrieves closing prices from the time series data for the specified dates.
     *
     * @param dates The list of dates for which to retrieve closing prices.
     * @param timeSeries The JSON object containing the time series data.
     * @return A list of closing prices.
     */
    private List<Double> getClosingPrices (List<String> dates, JsonObject timeSeries) {
        List<Double> closingPrices = new ArrayList<>();
        for (String date : dates) {
            double close = timeSeries.getAsJsonObject(date).get("4. close").getAsDouble();
            closingPrices.add(close);
        }
        return closingPrices;
    }

    /**
     * Prints analysis based on Simple Moving Average (SMA) indicators.
     *
     * @param closingPrices A list of closing prices.
     */
    private void printSMAAnalysis (List<Double> closingPrices) {
        // Calculate SMAs
        double smaShortTerm = SMAIndicator.calculateSMA(closingPrices, 20);
        double smaLongTerm = SMAIndicator.calculateSMA(closingPrices, 50);

        // Compares short-term and long-term SMAs to determine market trend
        if (smaShortTerm > smaLongTerm) {
            System.out.println("Short-term trend is upward compared to long-term.");
        } else if (smaShortTerm < smaLongTerm) {
            System.out.println("Short-term trend is downward compared to long-term.");
        } else {
            System.out.println("Trends are converging.");
        }
    }

    /**
     * Prints analysis based on the Relative Strength Index (RSI).
     *
     * @param closingPrices A list of closing prices.
     */
    private void printRSIAnalysis (List<Double> closingPrices) {

        double rsi = RSIIndicator.calculateRSI(closingPrices, 14);
        System.out.println("RSI (14-day): " + rsi);

        if (rsi > 70) {
            System.out.println("The stock is potentially overbought - the market might " +
                    "correct (price might go down).");
        } else if (rsi < 30) {
            System.out.println("The stock is potentially oversold - it might be a good buying" +
                    " opportunity.");
        } else {
            System.out.println("The stock is neither overbought nor oversold.");
        }
    }

    /**
     * Prints analysis based on Moving Average Convergence Divergence (MACD) values.
     *
     * @param closingPrices A list of closing prices.
     */
    private void printMACDAnalysis (List<Double> closingPrices){
        // Calculate MACD and analyse
        Map<String, List<Double>> macdResults = MACDIndicator.calculateMACD(closingPrices);
        List<Double> macdLine = macdResults.get("MACD Line");
        List<Double> signalLine = macdResults.get("Signal Line");
        List<Double> macdHistogram = macdResults.get("MACD Histogram");

        double lastMacdValue = macdLine.getLast();
        double lastSignalValue = signalLine.getLast();
        double lastHistogramValue = macdHistogram.getLast();

        if (lastMacdValue > lastSignalValue) {
            System.out.println("MACD is above the signal line - bullish signal.");
        } else {
            System.out.println("MACD is below the signal line - bearish signal.");
        }

        if (lastHistogramValue > 0) {
            System.out.println("MACD histogram is positive, indicating upward momentum.");
        } else {
            System.out.println("MACD histogram is negative, indicating downward momentum.");
        }
    }
}
