package pw.prok.damask.dsl;

import java.util.Arrays;

public final class Version implements Comparable<Version> {
    private final String mRawVersion;
    private final String[] mRawTokens;
    private final int[] mVersionTokens;
    private final boolean[] mUpVersion;
    private final boolean mSpecified;
    private final String mSnapshotVersion;
    private final String mFinalSnapshotVersion;

    public Version(String version) {
        mRawVersion = version;
        mRawTokens = version.split("\\.|-|_");
        mVersionTokens = new int[mRawTokens.length];
        mUpVersion = new boolean[mRawTokens.length];
        Arrays.fill(mVersionTokens, 0);
        Arrays.fill(mUpVersion, false);
        boolean specified = true;
        for (int i = 0; i < mRawTokens.length; i++) {
            String token = mRawTokens[i];
            if (token.endsWith("+")) {
                mUpVersion[i] = true;
                specified = false;
                token = token.substring(0, token.length() - 1);
            }
            try {
                mVersionTokens[i] = Integer.parseInt(token);
            } catch (NumberFormatException ignored) {
                mVersionTokens[i] = Integer.MIN_VALUE;
            }
        }
        mSnapshotVersion = null;
        mFinalSnapshotVersion = null;
        mSpecified = specified;
    }

    public Version(Version version) {
        this(version, null);
    }

    private Version(Version version, String snapshotVersion) {
        mRawVersion = version.mRawVersion;
        mRawTokens = version.mRawTokens;
        mVersionTokens = version.mVersionTokens;
        mUpVersion = version.mUpVersion;
        mSpecified = version.mSpecified;
        mSnapshotVersion = snapshotVersion == null ? version.mSnapshotVersion : snapshotVersion;
        if (mSnapshotVersion != null) {
            if (!mRawVersion.endsWith("-SNAPSHOT")) {
                throw new IllegalStateException("Current version (" + mRawVersion + ") isn't snapshot!");
            }
            mFinalSnapshotVersion = mRawVersion.substring(0, mRawVersion.length() - "SNAPSHOT".length()) + mSnapshotVersion;
        } else {
            mFinalSnapshotVersion = null;
        }
    }

    @Override
    public int compareTo(Version version) {
        final int l1 = mVersionTokens.length;
        final int l2 = version.mVersionTokens.length;
        int maxTokens = Math.max(l1, l2);
        for (int i = 0; i < maxTokens; i++) {
            int t1 = i < l1 ? mVersionTokens[i] : 0;
            int t2 = i < l2 ? version.mVersionTokens[i] : 0;
            if (t1 < t2) return -1;
            if (t1 > t2) return 1;
            int c = mRawTokens[i].compareTo(version.mRawTokens[i]);
            if (c != 0) return c;
        }
        return 0;
    }

    public boolean isMatching(Version version) {
        final int l1 = mVersionTokens.length;
        final int l2 = version.mVersionTokens.length;
        int maxTokens = Math.max(l1, l2);
        for (int i = 0; i < maxTokens; i++) {
            int t1 = i < l1 ? mVersionTokens[i] : 0;
            boolean p1 = i >= l1 || mUpVersion[i];
            int t2 = i < l2 ? version.mVersionTokens[i] : 0;
            boolean p2 = i >= l2 || version.mUpVersion[i];
            if (!(t1 == t2 && !p2 || p1 && t1 < t2)) return false;
        }
        return true;
    }

    public boolean isSpecified() {
        return mSpecified;
    }

    public boolean isSnapshot() {
        return mRawVersion.endsWith("-SNAPSHOT");
    }

    @Override
    public String toString() {
        if (mFinalSnapshotVersion != null) {
            return mRawVersion + '[' + mFinalSnapshotVersion + ']';
        }
        return mRawVersion;
    }

    @Override
    public int hashCode() {
        return mRawVersion.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Version)) return false;
        Version version = (Version) obj;
        return mRawVersion.equals(version.mRawVersion);
    }

    public Version resolveSnapshot(String timestamp, String buildNumber) {
        return resolveSnapshot(timestamp + '-' + buildNumber);
    }

    public Version resolveSnapshot(String snapshotVersion) {
        return new Version(this, snapshotVersion);
    }

    public String toRawString() {
        return mRawVersion;
    }

    public String toSnapshotString() {
        return mFinalSnapshotVersion != null ? mFinalSnapshotVersion : mRawVersion;
    }
}
