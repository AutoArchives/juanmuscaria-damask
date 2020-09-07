package pw.prok.damask.comparators;

import pw.prok.damask.dsl.IModule;

import java.util.Comparator;

public class ModuleComparator implements Comparator<IModule> {
    public static ModuleComparator INSTANCE = new ModuleComparator();

    @Override
    public int compare(IModule i1, IModule i2) {
        int c = compare(i1.getGroup(), i2.getGroup());
        if (c != 0) return c;
        return compare(i1.getName(), i2.getName());
    }

    public static int compare(String s1, String s2) {
        return s1 == null ? (s2 == null ? 0 : -1) : s1.compareTo(s2);
    }
}
