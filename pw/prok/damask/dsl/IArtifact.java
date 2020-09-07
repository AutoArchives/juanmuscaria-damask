package pw.prok.damask.dsl;

public interface IArtifact extends IModuleVersion{
    String getClassifier();

    String getExtension();

    IArtifact clone();
}
