package bill_calc;

public class BillingCalculator {

    // Calculate GST (3% of total)
    public static double calculateGST(double total) {
        return total * 0.03;
    }

    // Calculate return amount for old jewelry
    public static double calculateReturnAmount(double weight, double rate) {
        return weight * rate;
    }
// Adding some comments
    
// aDDING MORE COMMENTS 
    // Calculate final amount
    public static double calculateFinalAmount(double total, double gst, double returnAmount) {
        return (total + gst) - returnAmount;
    }

    // Convert number to words (simple version for INR)
    public static String convertNumberToWords(long number) {
        if (number == 0) return "Zero";

        String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                           "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                           "Eighteen", "Nineteen" };

        String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

        StringBuilder result = new StringBuilder();

        if (number / 100000 > 0) {
            result.append(convertNumberToWords(number / 100000)).append(" Lakh ");
            number %= 100000;
        }

        if (number / 1000 > 0) {
            result.append(convertNumberToWords(number / 1000)).append(" Thousand ");
            number %= 1000;
        }

        if (number / 100 > 0) {
            result.append(convertNumberToWords(number / 100)).append(" Hundred ");
            number %= 100;
        }

        if (number > 0) {
            if (number < 20) {
                result.append(units[(int) number]);
            } else {
                result.append(tens[(int) number / 10]);
                if (number % 10 > 0) {
                    result.append(" ").append(units[(int) number % 10]);
                }
            }
        }

        return result.toString().trim() + " Rupees Only";
    }
}
