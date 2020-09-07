package pw.prok.damask.api;

import pw.prok.damask.comparators.ModuleVersionComparator;
import pw.prok.damask.dsl.IModuleVersion;
import pw.prok.damask.internal.job.DamaskResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VersionListResult extends DamaskResult<VersionListResult> {
    private static final Comparator<IModuleVersion> LATEST_VERSION = ModuleVersionComparator.INSTANCE;
    private List<IModuleVersion> mVersions;

    public VersionListResult(List<IModuleVersion> versions) {
        mVersions = versions;
        Collections.sort(mVersions, ModuleVersionComparator.INSTANCE);
    }

    public List<IModuleVersion> getVersions() {
        return mVersions;
    }

    public IModuleVersion getLatestVersion() {
        return Collections.max(mVersions, LATEST_VERSION);
    }

    public IModuleVersion getFirstVersion() {
        return Collections.min(mVersions, LATEST_VERSION);
    }

    @Override
    public VersionListResult merge(VersionListResult result) {
        mVersions.addAll(result.mVersions);
        return this;
    }

    @Override
    public String toString() {
        return mVersions.toString();
    }
}
