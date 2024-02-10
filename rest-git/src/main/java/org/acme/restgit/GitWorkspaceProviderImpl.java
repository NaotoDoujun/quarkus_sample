package org.acme.restgit;

import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jgit.util.FileUtils;
import org.jboss.logging.Logger;

public class GitWorkspaceProviderImpl implements GitWorkspaceProvider {
    
    private static final Logger LOGGER = Logger.getLogger(GitWorkspaceProviderImpl.class.getName());
    private static final int DEFAULT_LOCK_TIMEOUT_SECONDS = 90;
    private final File rootDirectory;
    private final Lock directoryLock;
    private final int lockTimeoutSeconds;

    public GitWorkspaceProviderImpl(final File rootDirectory) {
        this(rootDirectory, DEFAULT_LOCK_TIMEOUT_SECONDS);
    }

    @SuppressWarnings("null")
    public GitWorkspaceProviderImpl(final File rootDirectory, final int lockTimeoutSeconds){
        this.rootDirectory =
        Preconditions.checkNotNull(rootDirectory, "Root Directory cannot be null");
        directoryLock = new ReentrantLock();
        Preconditions.checkArgument(
                rootDirectory.isDirectory(),
                "File %s should be a directory",
                rootDirectory.getAbsolutePath());
        Preconditions.checkArgument(
                rootDirectory.exists(), "File %s should exist", rootDirectory.getAbsolutePath());
        this.lockTimeoutSeconds = lockTimeoutSeconds;
    }

    @Override
    public File getRootDirectory() {
        return rootDirectory;
    }

    public boolean cleanWorkingDirectory() {
        synchronizedOperation(
                new Callable<Void>() {
                    @Override
                    public Void call() {
                        try {
                            FileUtils.delete(rootDirectory);
                        } catch (final IOException e) {
                            LOGGER.error("Unable to clean working directory", e);
                        }
                        return null;
                    }
                });
        return true;
    }

    @Override
    public <T> T synchronizedOperation(final Callable<T> callable) {
        try {
            if (directoryLock.tryLock(lockTimeoutSeconds, TimeUnit.SECONDS)) {
                try {
                    return callable.call();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    directoryLock.unlock();
                }
            } else {
                throw new RuntimeException(
                                "Attempt to acquire lock on working directory was timeout: "
                                 + lockTimeoutSeconds
                                 + "s. Maybe due to dead lock");
            }
        } catch (final InterruptedException e) {
            LOGGER.error("Thread interrupted. ", e);
        }
        return null;
    }
}
