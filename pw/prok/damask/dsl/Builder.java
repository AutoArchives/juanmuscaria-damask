package pw.prok.damask.dsl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Builder {
    public static final String DEFAULT_EXTENSION = "jar";

    private String mGroup;
    private String mName;
    private Version mVersion;
    private String mClassifier;
    private String mExtension;

    private Builder() {

    }

    public static Builder create() {
        return new Builder();
    }

    public Builder group(String group) {
        mGroup = group;
        return this;
    }

    public Builder name(String name) {
        mName = name;
        return this;
    }

    public Builder version(Version version) {
        mVersion = version;
        return this;
    }

    public Builder version(String version) {
        return version(version != null ? new Version(version) : null);
    }

    public Builder classifier(String classifier) {
        mClassifier = classifier;
        return this;
    }

    public Builder extension(String extension) {
        mExtension = extension;
        return this;
    }

    public Builder fromModule(IModule module) {
        mGroup = module.getGroup();
        mName = module.getName();
        return this;
    }

    public Builder fromModuleVersion(IModuleVersion moduleVersion) {
        fromModule(moduleVersion);
        mVersion = moduleVersion.getVersion();
        return this;
    }

    public Builder fromArtifact(IArtifact artifact) {
        fromModuleVersion(artifact);
        mClassifier = artifact.getClassifier();
        mExtension = artifact.getExtension();
        return this;
    }

    public IModule asModule() {
        return fillModule(new DefaultModule());
    }

    public IModuleVersion asModuleVersion() {
        return fillModuleVersion(new DefaultModuleVersion());
    }

    public IArtifact asArtifact() {
        return fillArtifact(new DefaultArtifact());
    }

    public <T extends DefaultModule> T fillModule(T module) {
        module.setGroup(mGroup);
        module.setName(mName);
        return module;
    }

    public <T extends DefaultModuleVersion> T fillModuleVersion(T moduleVersion) {
        moduleVersion.setVersion(mVersion);
        return fillModule(moduleVersion);
    }

    public <T extends DefaultArtifact> T fillArtifact(T artifact) {
        artifact.setClassifier(mClassifier);
        artifact.setExtension(mExtension != null ? mExtension : DEFAULT_EXTENSION);
        return fillModuleVersion(artifact);
    }

    public boolean enoughModuleVersion() {
        return mClassifier == null && mExtension == null;
    }

    public boolean enoughModule() {
        return enoughModuleVersion() && mVersion == null;
    }

    public IModule buildEnough() {
        if (enoughModule()) return asModule();
        else if (enoughModuleVersion()) return asModuleVersion();
        return asArtifact();
    }

    public Builder clear() {
        mVersion = null;
        mGroup = mName = mClassifier = mExtension = null;
        return this;
    }

    private static final Pattern ARTIFACT_PATTERN = Pattern.compile("^((?<group>[^:@]+):)?(?<name>[^:@]+)(:(?<version>[^:@]+)(:(?<classifier>[^:@]+))?)?(?<extension>@.+)?$");
    private static final String ARTIFACT_FORMAT = "[group:]name[:version[:classifier]][@extension]";

    public Builder parse(String s) {
        Matcher matcher = ARTIFACT_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not parse " + s + "! Required format: " + ARTIFACT_FORMAT);
        }
        group(matcher.group("group"));
        name(matcher.group("name"));
        version(matcher.group("version"));
        classifier(matcher.group("classifier"));
        extension(matcher.group("extension"));
        return this;
    }

    public static String asPath(IModule module) {
        return asPath(module, true, true);
    }

    public static String asPath(IModule module, boolean allowVersion, boolean allowArtifact) {
        return asPath(module, allowVersion, allowArtifact, null, null);
    }

    public static String asPath(IModule module, boolean allowVersion, boolean allowArtifact, IRepository repository) {
        return asPath(module, allowVersion, allowArtifact, repository, null);
    }

    public static String asPath(IModule module, boolean allowVersion, boolean allowArtifact, IRepository repository, String filename) {
        StringBuilder builder = new StringBuilder();
        boolean needFirstSlash = false;
        if (repository != null) {
            builder.append(repository.getURL());
            if (builder.charAt(builder.length() - 1) == '/') {
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        for (String s : module.getGroup().split("\\.")) {
            builder.append('/');
            builder.append(s);
        }
        builder.append('/');
        builder.append(module.getName());
        if (allowVersion && module instanceof IModuleVersion) {
            IModuleVersion moduleVersion = (IModuleVersion) module;
            builder.append('/');
            builder.append(moduleVersion.getVersion().toRawString());
            builder.append('/');
            if (allowArtifact && module instanceof IArtifact) {
                builder.append(toString((IArtifact) module));
            }
        }
        if (filename != null) {
            builder.append('/');
            builder.append(filename);
        }
        return builder.toString();
    }

    public static String toString(IArtifact artifact) {
        StringBuilder builder = new StringBuilder();
        builder.append(artifact.getName());
        builder.append('-');
        builder.append(artifact.getVersion().toSnapshotString());
        String classifier = artifact.getClassifier();
        if (classifier != null && classifier.length() > 0) {
            builder.append('-');
            builder.append(classifier);
        }
        builder.append('.');
        builder.append(artifact.getExtension());
        return builder.toString();
    }
}
