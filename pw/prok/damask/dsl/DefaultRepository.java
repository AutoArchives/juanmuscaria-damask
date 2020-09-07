package pw.prok.damask.dsl;

public class DefaultRepository implements IRepository {
    private String mName;
    private String mURL;

    public DefaultRepository() {

    }

    public DefaultRepository(String url) {
        this(null, url);
    }

    public DefaultRepository(String name, String url) {
        mName = name;
        mURL = url;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getURL() {
        return mURL;
    }

    public void setURL(String url) {
        mURL = url;
    }
}
