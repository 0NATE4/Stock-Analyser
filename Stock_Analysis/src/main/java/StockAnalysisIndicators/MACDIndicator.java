package StockAnalysisIndicators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class calculates the Moving Average Convergence Divergence (MACD) indicator,
 * which is widely used in stock market analysis. The MACD is a trend-following momentum
 * indicator that shows the relationship between two moving averages of a security's price.
 */
public class MACDIndicator {

    /**
     * Calculates the Exponential Moving Average (EMA) for a given set of prices.
     *
     * @param prices A list of closing prices for a security.
     * @param period The number of periods to use for the EMA calculation.
     * @param previousEMA The previous EMA value, used for recursive calculation.
     * @return The calculated EMA for the current period.
     */
    private static double calculateEMA(List<Double> prices, int period, double previousEMA) {
        double alpha = 2.0 / (period + 1);
        double close = prices.getLast(); // Most recent price
        return alpha * close + (1 - alpha) * previousEMA;
    }

    /**
     * Calculates the MACD line, Signal line, and MACD Histogram for a given set of closing prices.
     *
     * @param closingPrices A list of closing prices for a security.
     * @return A map containing the MACD line, Signal line, and MACD Histogram.
     */
    public static Map<String, List<Double>> calculateMACD(List<Double> closingPrices) {
        int shortPeriod = 12;
        int longPeriod = 26;
        int signalPeriod = 9;
        List<Double> macdLine = new ArrayList<>();
        List<Double> signalLine = new ArrayList<>();
        List<Double> macdHistogram = new ArrayList<>();

        // Initial EMA calculation using the average of the first `shortPeriod`
        // and `longPeriod` closing prices.
        double shortEma =
                closingPrices.stream()
                        .limit(shortPeriod)
                        .mapToDouble(d -> d)
                        .average()
                        .orElse(Double.NaN);
        double longEma =
                closingPrices.stream()
                        .limit(longPeriod)
                        .mapToDouble(d -> d)
                        .average()
                        .orElse(Double.NaN);

        for (int i = longPeriod; i < closingPrices.size(); i++) {
            // Update the EMA values for each period after the initial calculation.
            shortEma = calculateEMA(closingPrices.subList(i - shortPeriod + 1, i + 1),
                    shortPeriod, shortEma);
            longEma = calculateEMA(closingPrices.subList(i - longPeriod + 1, i + 1),
                        longPeriod, longEma);

            // Calculate the MACD value and update the MACD line, Signal line, and MACD Histogram
            // accordingly.
            double macd = shortEma - longEma;
            macdLine.add(macd);

            if (macdLine.size() >= signalPeriod) {
                double signalEma;
                if (macdLine.size() == signalPeriod) {
                    signalEma = macdLine.stream().mapToDouble(d -> d).average().orElse(Double.NaN);
                } else {
                    signalEma = calculateEMA(macdLine.subList(macdLine.size() - signalPeriod,
                            macdLine.size()), signalPeriod, signalLine.get(signalLine.size() - 1));
                }
                signalLine.add(signalEma);
                macdHistogram.add(macd - signalEma);
            }
        }

        Map<String, List<Double>> macdResults = new HashMap<>();
        macdResults.put("MACD Line", macdLine);
        macdResults.put("Signal Line", signalLine);
        macdResults.put("MACD Histogram", macdHistogram);

        return macdResults;
    }
}
