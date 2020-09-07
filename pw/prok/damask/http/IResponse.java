package pw.prok.damask.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface IResponse {
    int status();

    Map<String, String[]> headers();

    InputStream content();

    long contentLength();

    String[] headers(String name);

    String header(String name);

    byte[] asBytes();

    String asString();

    IResponse save(File file) throws IOException;

    IResponse save(OutputStream os) throws IOException;
}
