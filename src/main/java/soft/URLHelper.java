package soft;

import okhttp3.internal.http2.Http2Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLHelper {
    public static int getPageResponseCode(String pageUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pageUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();

            return code;
        } catch (IOException e) {
            e.printStackTrace();

            return -1;
        } finally {
            connection.disconnect();
        }
    }
}
