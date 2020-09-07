package pw.prok.damask.internal.job;

public abstract class DamaskResult<Result extends DamaskResult<Result>> {
    public abstract Result merge(Result result);
}
