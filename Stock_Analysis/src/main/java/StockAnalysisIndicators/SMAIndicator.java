package StockAnalysisIndicators;

import java.util.List;

/**
 * Provides calculation for the Simple Moving Average (SMA), which is a widely used indicator
 * in stock market analysis to help determine the direction of market trends.
 */
public class SMAIndicator {

    /**
     * Calculates the Simple Moving Average (SMA) for a given list of closing prices
     * over a specified period.
     *
     * @param closingPrices A list containing the closing prices of a stock or asset.
     * @param days The period over which to calculate the SMA. This value determines how many of
     *             the most recent closing prices will be included in the average calculation.
     * @return The calculated SMA value. Returns -1 if the list of closing prices contains fewer
     * elements than the specified period, indicating there's not enough data to perform the
     * calculation.
     */
    public static double calculateSMA(List<Double> closingPrices, int days) {
        if (closingPrices.size() < days) return -1; // Not enough data
        return closingPrices.stream()
                .limit(days)
                .mapToDouble(d -> d)
                .average()
                .orElse(Double.NaN);
    }
}
