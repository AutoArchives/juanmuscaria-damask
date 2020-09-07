package pw.prok.damask.internal.job;

import org.w3c.dom.Document;
import pw.prok.damask.Damask;
import pw.prok.damask.dsl.IRepository;
import pw.prok.damask.http.IResponse;
import pw.prok.damask.http.SimpleHttp;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public abstract class DamaskRequest<Result extends DamaskResult<Result>> {
    public abstract Result performRequest(Damask damask, IRepository repository) throws Exception;

    public Document parse(String url) throws Exception {
        return parse(SimpleHttp.request(url));
    }

    public Document parse(IResponse response) throws Exception {
        return parse(response.content());
    }

    public Document parse(InputStream is) throws Exception {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    }
}
