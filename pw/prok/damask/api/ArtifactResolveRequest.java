package pw.prok.damask.api;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import pw.prok.damask.Damask;
import pw.prok.damask.dsl.Builder;
import pw.prok.damask.dsl.IArtifact;
import pw.prok.damask.dsl.IRepository;
import pw.prok.damask.dsl.Version;
import pw.prok.damask.http.IResponse;
import pw.prok.damask.http.SimpleHttp;
import pw.prok.damask.internal.job.DamaskRequest;
import pw.prok.damask.utils.Digest;

import javax.xml.xpath.XPathConstants;
import java.io.File;

public class ArtifactResolveRequest extends DamaskRequest<ArtifactResolveResult> {
    private final IArtifact mArtifact;
    private final File mTargetFile;
    private final boolean mAutoname;

    public ArtifactResolveRequest(IArtifact artifact, File targetFile, boolean autoname) {
        mArtifact = artifact;
        mTargetFile = targetFile;
        mAutoname = autoname;
    }

    @Override
    public ArtifactResolveResult performRequest(Damask damask, IRepository repository) throws Exception {
        IArtifact artifact = mArtifact;
        if (!artifact.getVersion().isSpecified()) {
            Damask repositoryDamask = new Damask();
            repositoryDamask.addRepository(repository);
            VersionListResult result = repositoryDamask.versionList(artifact);
            artifact = Builder.create().fromArtifact(artifact).version(result.getLatestVersion().getVersion()).asArtifact();
        }
        Version artifactVersion = artifact.getVersion();
        if (artifactVersion.isSnapshot()) {
            Document document = parse(Builder.asPath(mArtifact, true, false, repository) + "maven-metadata.xml");
            Node node = (Node) Damask.x().evaluate("/metadata/versioning/snapshot", document, XPathConstants.NODE);
            String timestamp = (String) Damask.x().evaluate("timestamp/text()", node, XPathConstants.STRING);
            String buildNumber = (String) Damask.x().evaluate("buildNumber/text()", node, XPathConstants.STRING);
            artifactVersion = artifactVersion.resolveSnapshot(timestamp, buildNumber);
            artifact = Builder.create().fromArtifact(artifact).version(artifactVersion).asArtifact();
        }
        final String filename = Builder.toString(artifact);
        File targetFile = mTargetFile;
        if (targetFile == null) {
            targetFile = Damask.temp(artifact);
        }
        if (mAutoname) {
            targetFile = new File(targetFile, filename);
        }
        String path = Builder.asPath(artifact, true, true, repository);
        IResponse response = SimpleHttp.request(path);
        if (response.status() != 200) return null;
        response.save(targetFile);
        checkDigest(path, targetFile, Digest.DigestType.MD5);
        checkDigest(path, targetFile, Digest.DigestType.SHA1);
        return new ArtifactResolveResult(artifact, targetFile, filename);
    }

    private void checkDigest(String path, File targetFile, Digest.DigestType digestType) throws Exception {
        IResponse response = SimpleHttp.request(path + '.' + digestType.name().toLowerCase());
        if (response.status() == 404) {
            return;
        }
        String expected = response.asString().trim();
        String actual = Digest.toHex(Digest.digest(targetFile, digestType));
        if (!expected.equalsIgnoreCase(actual)) {
            throw new IllegalStateException("Digest " + digestType.getType() + " reports that something wrong!");
        }
    }

    public static void main(String[] args) {
        Damask damask = new Damask();
        damask.addRepository("prok", "https://repo.prok.pw/");
        damask.artifactResolve(Builder.create().group("net.md-5").name("SpecialSource").version("1.7-SNAPSHOT").asArtifact());
    }
}
