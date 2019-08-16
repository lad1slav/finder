package soft;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Validator {

    public static void isNotNegative(Double num) {
        if (num < 0)
        {
            throw new IllegalArgumentException("Number " + num + " must be greatest than null or equals them");
        }
    }

    public static void isNotNegative(Long num) {
        if (num < 0)
        {
            throw new IllegalArgumentException("Number " + num + " must be greatest than null or equals them");
        }
    }
}
