package pw.prok.damask;

import pw.prok.damask.api.ArtifactResolveRequest;
import pw.prok.damask.api.ArtifactResolveResult;
import pw.prok.damask.api.VersionListRequest;
import pw.prok.damask.api.VersionListResult;
import pw.prok.damask.cache.SimpleCache;
import pw.prok.damask.dsl.DefaultRepository;
import pw.prok.damask.dsl.IArtifact;
import pw.prok.damask.dsl.IModule;
import pw.prok.damask.dsl.IRepository;
import pw.prok.damask.internal.job.DamaskExecutor;
import pw.prok.damask.internal.job.DamaskRequest;
import pw.prok.damask.internal.job.DamaskResult;
import pw.prok.damask.utils.Digest;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Damask {
    private static final XPathFactory XPATH_FACTORY;
    private static final XPath XPATH;
    private static final SimpleCache<Object> sCache;
    private static final Damask DEFAULT;
    private static File sTempDirectory;

    static {
        XPATH_FACTORY = XPathFactory.newInstance();
        XPATH = XPATH_FACTORY.newXPath();
        sCache = SimpleCache.memoryCache();
        DEFAULT = new Damask();
    }

    public static XPath x() {
        return XPATH;
    }

    public static File temp(String id) {
        if (sTempDirectory == null) {
            try {
                File baseDir = new File(System.getProperty("java.io.tmpdir"));
                sTempDirectory = new File(baseDir, "damask-" + new Random().nextLong() + "-" + System.nanoTime());
                if (!sTempDirectory.mkdirs()) throw new IOException("Could not create directory set");
            } catch (IOException e) {
                throw new RuntimeException("Sometimes we can create directories. Sometimes not. It's not your day buddy.", e);
            }
        }
        File f = new File(sTempDirectory, Digest.toHex(Digest.sha1(id)));
        f.mkdirs();
        return f;
    }

    public static File temp(Object id) {
        return temp(String.valueOf(System.identityHashCode(id)));
    }

    public static Damask get() {
        return DEFAULT;
    }

    private List<IRepository> mRepositories = new LinkedList<IRepository>();

    public Damask addRepository(IRepository repository) {
        mRepositories.add(repository);
        return this;
    }

    public Damask addRepository(String name, String url) {
        return addRepository(new DefaultRepository(name, url));
    }

    public List<IRepository> repositories() {
        return Collections.unmodifiableList(mRepositories);
    }

    public <Request extends DamaskRequest<Result>, Result extends DamaskResult<Result>> Result execute(Request request) {
        return DamaskExecutor.execute(request, this);
    }

    public VersionListResult versionList(IModule module) {
        return execute(new VersionListRequest(module));
    }

    public ArtifactResolveResult artifactResolve(IArtifact artifact, File targetFile, boolean autoname) {
        return execute(new ArtifactResolveRequest(artifact, targetFile, autoname));
    }

    public ArtifactResolveResult artifactResolve(IArtifact artifact) {
        return execute(new ArtifactResolveRequest(artifact, null, true));
    }
}
