package pw.prok.damask.api;

import pw.prok.damask.dsl.IArtifact;
import pw.prok.damask.internal.job.DamaskResult;

import java.io.File;

public class ArtifactResolveResult extends DamaskResult<ArtifactResolveResult> {
    private IArtifact mArtifact;
    private File mFile;
    private String mFilename;

    public ArtifactResolveResult(IArtifact artifact, File file, String filename) {
        mArtifact = artifact;
        mFile = file;
        mFilename = filename;
    }

    @Override
    public ArtifactResolveResult merge(ArtifactResolveResult result) {
        if (mArtifact == null && result.mArtifact != null) {
            mArtifact = result.mArtifact;
            mFile = result.mFile;
            mFilename = result.mFilename;
        }
        return this;
    }
}
