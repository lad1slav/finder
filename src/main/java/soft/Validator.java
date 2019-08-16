package soft;

public class Validator {
    private Validator() {}

    public static void isNotNegative(Double num) {
        if (num < 0)
        {
            throw new IllegalArgumentException("Number " + num + " must be greatest than null or equals them");
        }
    }
}
