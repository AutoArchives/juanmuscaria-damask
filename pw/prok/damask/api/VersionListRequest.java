package pw.prok.damask.api;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import pw.prok.damask.Damask;
import pw.prok.damask.dsl.*;
import pw.prok.damask.internal.job.DamaskRequest;

import javax.xml.xpath.XPathConstants;
import java.util.LinkedList;
import java.util.List;

public class VersionListRequest extends DamaskRequest<VersionListResult> {
    public IModule mModule;

    public VersionListRequest(IModule module) {
        mModule = module;
    }

    @Override
    public VersionListResult performRequest(Damask damask, IRepository repository) throws Exception {
        Version matching = null;
        if (mModule instanceof IModuleVersion) {
            matching = ((IModuleVersion) mModule).getVersion();
        }
        List<IModuleVersion> versions = new LinkedList<IModuleVersion>();
        Document document = parse(Builder.asPath(mModule, false, false, repository, "maven-metadata.xml"));
        NodeList nodes = (NodeList) Damask.x().evaluate("/metadata/versioning/versions[*]/version", document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            IModuleVersion candidate = Builder.create().fromModule(mModule).version(nodes.item(i).getFirstChild().getNodeValue()).asModuleVersion();
            if (matching != null && !matching.isMatching(candidate.getVersion()))
                continue;
            versions.add(candidate);
        }
        return new VersionListResult(versions);
    }
}
