package pw.prok.damask.internal.job;

import pw.prok.damask.Damask;
import pw.prok.damask.dsl.IRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DamaskExecutor<Request extends DamaskRequest<Result>, Result extends DamaskResult<Result>> implements Runnable {
    private static Executor sExecutor = Executors.newFixedThreadPool(5, new ThreadFactory() {
        private int executorId = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = Executors.defaultThreadFactory().newThread(runnable);
            t.setName("Damask Executor #" + executorId++);
            t.setDaemon(true);
            return t;
        }
    });
    private final Request mRequest;
    private final Damask mDamask;
    private final ResultMerger<Result> mMerger;
    private final IRepository mRepository;

    public DamaskExecutor(Request request, Damask damask, ResultMerger<Result> merger, IRepository repository) {
        mRequest = request;
        mDamask = damask;
        mMerger = merger;
        mRepository = repository;
    }

    public static <Request extends DamaskRequest<Result>, Result extends DamaskResult<Result>> Result execute(Request request, Damask damask) {
        List<IRepository> repositories = damask.repositories();
        ResultMerger<Result> merger = new ResultMerger<Result>(repositories.size());
        for (IRepository repository : repositories)
            sExecutor.execute(new DamaskExecutor<Request, Result>(request, damask, merger, repository));
        while (!merger.ready())
            Thread.yield();
        return merger.result();
    }

    @Override
    public void run() {
        try {
            mMerger.addResult(mRequest.performRequest(mDamask, mRepository));
        } catch (Exception e) {
            if ("true".equals(System.getProperty("damask.verbose")))
                e.printStackTrace();
        } finally {
            mMerger.tick();
        }
    }
}
