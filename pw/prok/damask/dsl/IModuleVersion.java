package pw.prok.damask.dsl;

public interface IModuleVersion extends IModule {
    Version getVersion();

    IModuleVersion clone();
}
