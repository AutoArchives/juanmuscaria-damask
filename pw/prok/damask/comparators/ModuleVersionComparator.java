package pw.prok.damask.comparators;

import pw.prok.damask.dsl.IModuleVersion;

import java.util.Comparator;

public class ModuleVersionComparator implements Comparator<IModuleVersion> {
    public static ModuleVersionComparator INSTANCE = new ModuleVersionComparator();

    @Override
    public int compare(IModuleVersion i1, IModuleVersion i2) {
        int c = ModuleComparator.INSTANCE.compare(i1, i2);
        if (c != 0) return c;
        return i1.getVersion().compareTo(i2.getVersion());
    }
}
