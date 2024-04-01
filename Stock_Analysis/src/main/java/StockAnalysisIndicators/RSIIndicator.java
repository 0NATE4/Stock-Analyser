package StockAnalysisIndicators;

import java.util.List;

/**
 * Calculates the Relative Strength Index (RSI), a momentum indicator that measures the magnitude
 * of recent price changes to evaluate overbought or oversold conditions in the price of a stock
 * or other asset.
 */
public class RSIIndicator {

    /**
     * Calculates the Relative Strength Index (RSI) for a given list of closing prices
     * over a specified number of days.
     *
     * @param closingPrices A list of closing prices for a security.
     * @param days The period over which the RSI is calculated, typically 14 days.
     * @return The RSI value. Returns -1 if there is not enough data (fewer prices than days).
     */
    public static double calculateRSI(List<Double> closingPrices, int days) {
        if (closingPrices.size() <= days) return -1; // Not enough data

        double averageGain = 0, averageLoss = 0;
        // Initially calculate the first average gain and loss
        for (int i = 1; i <= days; i++) {
            double change = closingPrices.get(i) - closingPrices.get(i - 1);
            if (change > 0) {
                averageGain += change;
            } else {
                averageLoss += Math.abs(change);
            }
        }
        averageGain /= days;
        averageLoss /= days;

        // Use the first average gain and loss as the initial EMA values
        double emaGain = averageGain;
        double emaLoss = averageLoss;

        // Continue calculating EMA for the rest of the days
        for (int i = days + 1; i < closingPrices.size(); i++) {
            double change = closingPrices.get(i) - closingPrices.get(i - 1);
            if (change > 0) {
                emaGain = (change * (2.0 / (1 + days))) + (emaGain * (1 - (2.0 / (1 + days))));
                emaLoss = (0 * (2.0 / (1 + days))) + (emaLoss * (1 - (2.0 / (1 + days))));
            } else {
                emaLoss =
                        (Math.abs(change) * (2.0 / (1 + days))) + (emaLoss * (1 - (2.0 / (1 + days))));
                emaGain = (0 * (2.0 / (1 + days))) + (emaGain * (1 - (2.0 / (1 + days))));
            }
        }

        // Calculate the Relative Strength (RS) value.
        double rs = (emaLoss == 0) ? Double.POSITIVE_INFINITY : (emaGain / emaLoss);
        return 100 - (100 / (1 + rs));
    }
}
