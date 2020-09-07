package pw.prok.damask.dsl;

public class DefaultModule implements IModule {
    protected String mGroup;
    protected String mName;

    @Override
    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public DefaultModule clone() {
        try {
            DefaultModule module = (DefaultModule) super.clone();
            module.mGroup = mGroup;
            module.mName = mName;
            return module;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IModule)) return false;
        IModule that = (IModule) o;
        if (mGroup != null ? !mGroup.equals(that.getGroup()) : that.getGroup() != null) return false;
        if (mName != null ? !mName.equals(that.getName()) : that.getName() != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = mGroup != null ? mGroup.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return mGroup + ':' + mName;
    }
}
