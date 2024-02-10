package org.acme.restgit;

import java.io.File;
import java.util.concurrent.Callable;

public interface GitWorkspaceProvider {
    
    boolean cleanWorkingDirectory();

    File getRootDirectory();

    <T> T synchronizedOperation(final Callable<T> callable);

}
