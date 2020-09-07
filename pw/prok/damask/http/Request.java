package pw.prok.damask.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String url;
    private String contentType;
    private InputStream content;
    private long contentLength = -1;
    private Map<String, String[]> headers;

    public static Request get(String url) {
        Request request = new Request();
        request.method = "GET";
        request.url = url;
        return request;
    }

    public Request clearHeaders() {
        headers = null;
        return this;
    }

    public Request addHeader(String name, String value) {
        if (value == null) return this;
        accureHeaders();
        String[] s = headers.get(name);
        if (s == null || s.length == 0) {
            s = new String[]{value};
        } else {
            String[] ns = new String[s.length + 1];
            System.arraycopy(s, 0, ns, 0, s.length);
            ns[s.length] = value;
            s = ns;
        }
        headers.put(name, s);
        return this;
    }

    public Request header(String name, String value) {
        accureHeaders();
        if (value == null) {
            headers.remove(name);
        } else {
            headers.put(name, new String[]{value});
        }
        return this;
    }

    public Request removeHeader(String name) {
        if (headers == null) return this;
        return header(name, null);
    }

    private void accureHeaders() {
        if (headers == null) {
            headers = new HashMap<String, String[]>();
        }
    }

    public Request headers(Map<String, String[]> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String[]> headers() {
        return headers;
    }

    public Request url(String url) {
        this.url = url;
        return this;
    }

    public String url() {
        return url;
    }

    public String method() {
        return method;
    }

    public Request method(String method) {
        this.method = method;
        return this;
    }

    public Request get() {
        this.method = "GET";
        this.content = null;
        this.contentLength = -1;
        this.contentType = null;
        return this;
    }

    public Request post(InputStream content, long contentLength, String contentType) {
        this.method = "POST";
        this.content = content;
        this.contentLength = contentLength;
        this.contentType = contentType;
        return this;
    }


    public InputStream content() {
        return content;
    }

    public Request content(InputStream content) {
        this.content = content;
        return this;
    }

    public long contentLength() {
        return contentLength;
    }

    public Request contentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String contentType() {
        return contentType;
    }

    public Request contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    private byte[] cacheContent;

    public void cacheContent(byte[] cacheContent) {
        this.cacheContent = cacheContent;
    }

    public void prepare() {
        if (cacheContent != null) {
            content = new ByteArrayInputStream(cacheContent);
            contentLength = cacheContent.length;
        }
    }
}
