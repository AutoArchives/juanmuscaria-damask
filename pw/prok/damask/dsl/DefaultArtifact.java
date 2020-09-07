package pw.prok.damask.dsl;

public class DefaultArtifact extends DefaultModuleVersion implements IArtifact {
    protected String mClassifier;
    protected String mExtension;

    @Override
    public String getClassifier() {
        return mClassifier;
    }

    public void setClassifier(String classifier) {
        mClassifier = classifier;
    }

    @Override
    public String getExtension() {
        return mExtension;
    }

    public void setExtension(String extension) {
        mExtension = extension;
    }

    @Override
    public DefaultArtifact clone() {
        DefaultArtifact artifact = (DefaultArtifact) super.clone();
        artifact.mClassifier = mClassifier;
        artifact.mExtension = mExtension;
        return artifact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IModuleVersion)) return false;
        IArtifact that = (IArtifact) o;
        if (mGroup != null ? !mGroup.equals(that.getGroup()) : that.getGroup() != null) return false;
        if (mName != null ? !mName.equals(that.getName()) : that.getName() != null) return false;
        if (mVersion != null ? !mVersion.equals(that.getVersion()) : that.getVersion() != null) return false;
        if (mClassifier != null ? !mClassifier.equals(that.getClassifier()) : that.getClassifier() != null)
            return false;
        if (mExtension != null ? !mExtension.equals(that.getExtension()) : that.getExtension() != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = mGroup != null ? mGroup.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mVersion != null ? mVersion.hashCode() : 0);
        result = 31 * result + (mClassifier != null ? mClassifier.hashCode() : 0);
        result = 31 * result + (mExtension != null ? mExtension.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String s = super.toString();
        if (mClassifier != null) {
            s += ':' + mClassifier;
        }
        if (mExtension != null && !Builder.DEFAULT_EXTENSION.equals(mExtension)) {
            s += '@' + mExtension;
        }
        return s;
    }
}
