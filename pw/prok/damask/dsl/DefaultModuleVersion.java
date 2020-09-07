package pw.prok.damask.dsl;

public class DefaultModuleVersion extends DefaultModule implements IModuleVersion {
    protected Version mVersion;

    @Override
    public Version getVersion() {
        return mVersion;
    }

    public void setVersion(Version version) {
        mVersion = version;
    }

    @Override
    public DefaultModuleVersion clone() {
        DefaultModuleVersion module = (DefaultModuleVersion) super.clone();
        module.mVersion = mVersion;
        return module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IModuleVersion)) return false;
        IModuleVersion that = (IModuleVersion) o;
        if (mGroup != null ? !mGroup.equals(that.getGroup()) : that.getGroup() != null) return false;
        if (mName != null ? !mName.equals(that.getName()) : that.getName() != null) return false;
        if (mVersion != null ? !mVersion.equals(that.getVersion()) : that.getVersion() != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = mGroup != null ? mGroup.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mVersion != null ? mVersion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + ':' + mVersion;
    }
}
