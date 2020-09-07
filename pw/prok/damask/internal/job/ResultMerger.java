package pw.prok.damask.internal.job;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResultMerger<Result extends DamaskResult<Result>> {
    private final int mMaxTicks;
    private Result mResult;
    private Lock mLock = new ReentrantLock();
    private AtomicInteger mTicks = new AtomicInteger();

    public ResultMerger(int maxTicks) {
        mMaxTicks = maxTicks;
    }

    public void addResult(Result result) {
        mLock.lock();
        try {
            if (mResult == null) {
                mResult = result;
            } else if (result != null) {
                mResult.merge(result);
            }
        } finally {
            mLock.unlock();
        }
    }

    public void tick() {
        mLock.lock();
        try {
            mTicks.incrementAndGet();
        } finally {
            mLock.unlock();
        }
    }

    public boolean ready() {
        return mTicks.get() == mMaxTicks;
    }

    public Result result() {
        return mResult;
    }
}
