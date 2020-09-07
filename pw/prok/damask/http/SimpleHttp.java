package pw.prok.damask.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHttp {
    private static final int DEFAULT_MAX_TRIES = 3;

    public static IResponse request(String url) throws IOException {
        return request(Request.get(url));
    }

    public static IResponse request(Request request) throws IOException {
        return request(request, DEFAULT_MAX_TRIES);
    }

    public static IResponse request(Request request, int maxTries) throws IOException {
        Response response = new Response();
        try {
            URL url = new URL(request.url());
            URLConnection connection = url.openConnection();
            configureHeaders(connection, request.headers());
            HttpURLConnection httpURLConnection = connection instanceof HttpURLConnection ? (HttpURLConnection) connection : null;
            if (httpURLConnection != null) {
                httpURLConnection.setRequestMethod(request.method());
                httpURLConnection.setInstanceFollowRedirects(true);
            }
            InputStream content = request.content();
            if (content != null) {
                connection.setDoOutput(true);
                if (httpURLConnection != null) {
                    long contentLength = request.contentLength();
                    if (contentLength == -1) contentLength = content.available();
                    if (contentLength == -1) {
                        final byte[] bytes = cache(content);
                        request.cacheContent(bytes);
                        request.prepare();
                        content = request.content();
                        contentLength = request.contentLength();
                    }
                    httpURLConnection.setFixedLengthStreamingMode(contentLength);
                }
            }
            connection.connect();
            if (content != null) {
                OutputStream os = connection.getOutputStream();
                copyStream(content, os);
            }
            content = connection.getInputStream();
            byte[] result = cache(content);
            if (httpURLConnection != null) {
                response.status(httpURLConnection.getResponseCode());
            }
            response.headers(convertHeaders(connection.getHeaderFields()));
            response.content(new ByteArrayInputStream(result));
            response.contentLength(result.length);
            return response;
        } catch (FileNotFoundException e) {
            response.status(404);
            return response;
        } catch (IOException e) {
            if (maxTries <= 1) {
                throw e;
            }
            request.prepare();
            return request(request, maxTries - 1);
        }
    }

    private static Map<String, String[]> convertHeaders(Map<String, List<String>> raw) {
        Map<String, String[]> headers = new HashMap<String, String[]>();
        for (Map.Entry<String, List<String>> entry : raw.entrySet()) {
            List<String> values = entry.getValue();
            headers.put(entry.getKey(), values.toArray(new String[values.size()]));
        }
        return headers;
    }

    private static void configureHeaders(URLConnection connection, Map<String, String[]> headers) {
        if (headers == null) return;
        for (Map.Entry<String, String[]> entry : headers.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            boolean first = true;
            for (String value : values) {
                if (value == null) continue;
                if (first) {
                    connection.setRequestProperty(name, value);
                    first = false;
                } else {
                    connection.addRequestProperty(name, value);
                }
            }
        }
    }

    public static byte[] cache(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        copyStream(is, os);
        return os.toByteArray();
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[4096];
        int c;
        while ((c = is.read(buffer)) > 0) {
            os.write(buffer, 0, c);
        }
        os.flush();
    }
}
