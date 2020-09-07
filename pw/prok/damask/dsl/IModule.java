package pw.prok.damask.dsl;

public interface IModule extends Cloneable {
    String getGroup();

    String getName();

    IModule clone();
}
