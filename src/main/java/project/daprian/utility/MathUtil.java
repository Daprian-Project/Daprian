package project.daprian.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static int getDecimalPlaces(double number) {
        String numberStr = Double.toString(number);
        int decimalIndex = numberStr.indexOf('.');
        if (decimalIndex < 0) {
            return 0; // No decimal point found, so there are no decimal places.
        } else {
            return numberStr.length() - decimalIndex - 1;
        }
    }

    public static double roundDecimalPlaces(double number, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be non-negative.");
        }

        BigDecimal bigDecimal = new BigDecimal(Double.toString(number));
        bigDecimal = bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }

}