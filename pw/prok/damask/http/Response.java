package pw.prok.damask.http;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class Response implements IResponse {
    private int status;
    private Map<String, String[]> headers;
    private InputStream content;
    private long contentLength;
    private byte[] contentBytes;

    @Override
    public int status() {
        return status;
    }

    public Response status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public Map<String, String[]> headers() {
        return headers;
    }

    public Response headers(Map<String, String[]> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public InputStream content() {
        return content;
    }

    public Response content(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    public Response contentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    @Override
    public String[] headers(String name) {
        if (headers == null) return null;
        return headers.get(name);
    }

    @Override
    public String header(String name) {
        final String[] headers = headers(name);
        return headers != null && headers.length > 0 ? headers[0] : null;
    }

    public Response content(byte[] bytes) {
        this.content = new ByteArrayInputStream(bytes);
        this.contentBytes = bytes;
        return this;
    }

    @Override
    public byte[] asBytes() {
        try {
            if (contentBytes != null) return contentBytes;
            return contentBytes = SimpleHttp.cache(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final Charset UTF_8 = Charset.forName("utf-8");

    @Override
    public String asString() {
        return new String(asBytes(), UTF_8);
    }

    @Override
    public IResponse save(File file) throws IOException {
        if (content == null) throw new IllegalStateException("Content is null, reject saving");
        return save(new FileOutputStream(file));
    }

    @Override
    public IResponse save(OutputStream os) throws IOException {
        if (content == null) throw new IllegalStateException("Content is null, reject saving");
        SimpleHttp.copyStream(content, os);
        os.close();
        return this;
    }
}
