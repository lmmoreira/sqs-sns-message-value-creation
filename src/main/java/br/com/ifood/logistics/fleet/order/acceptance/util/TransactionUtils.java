package br.com.ccrs.logistics.fleet.order.acceptance.util;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public final class TransactionUtils {

    private TransactionUtils() {
    }

    /**
     * Executes the given task after the current transaction is committed.
     *
     * @param task the task to be executed.
     */
    public static void afterCommit(final Runnable task) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                task.run();
            }
        });
    }

}

