package pw.prok.damask.comparators;

import pw.prok.damask.dsl.IArtifact;

import java.util.Comparator;

public class ArtifactComparator implements Comparator<IArtifact> {
    public static final ArtifactComparator INSTANCE = new ArtifactComparator();

    @Override
    public int compare(IArtifact i1, IArtifact i2) {
        int c = ModuleVersionComparator.INSTANCE.compare(i1, i2);
        if (c != 0) return c;
        c = ModuleComparator.compare(i1.getClassifier(), i2.getClassifier());
        if (c != 0) return c;
        return ModuleComparator.compare(i1.getExtension(), i2.getExtension());
    }
}
