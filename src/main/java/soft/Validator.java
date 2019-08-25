package soft;

import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@NoArgsConstructor
public class Validator {

    public static URL isCorrectURL(String url) {
        try {
            return new URL(new URI(url).toString());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException(url + "\n" + e.getMessage());
        }
    }

    public static double isNotNegative(Double num) {
        if (num < 0)
        {
            throw new IllegalArgumentException("Number " + num + " must be greatest than null or equals them");
        }

        return num;
    }
}
