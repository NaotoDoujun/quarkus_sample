package org.acme.restgit;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Objects;
import java.util.concurrent.Callable;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.logging.Logger;

import com.google.common.io.Files;

@Path("/git")
public class GitResource {

    private static final Logger LOGGER = Logger.getLogger(GitResource.class.getName());
    
    // not sure why but didnt work annotation.
    private String remoteURL = ConfigProvider.getConfig().getValue("git.repository.remote.url", String.class);
    private String rootDir = ConfigProvider.getConfig().getValue("git.repository.local.path", String.class);

    private GitWorkspaceProvider workspaceProvider;
    private UsernamePasswordCredentialsProvider user;
    private Git git;

    public GitResource() {
        final File workingDir = new File(rootDir);
        if (!workingDir.exists()) {
            workingDir.mkdirs();
            LOGGER.info("created workingDir = " + rootDir);
        }
        workspaceProvider = new GitWorkspaceProviderImpl(workingDir);
        user = new UsernamePasswordCredentialsProvider("root", "P@ssw0rd");
        initializeRepository();
    }

    @POST
    public Response add(@RestForm String description, @RestForm("file") FileUpload file) throws IOException {
        if (Objects.nonNull(git)) {
            try {
                final File workingDir = workspaceProvider.getRootDirectory();
                Files.move(file.uploadedFile().toFile(), new File(FileSystems.getDefault().getPath(workingDir.toString(), file.fileName()).toString()));
                git.add().addFilepattern(".").call();
                LOGGER.info("added filename = " + file.fileName());
                return Response.ok().build();
            } catch (GitAPIException e) {
                LOGGER.error(e);
            }
        }
        return Response.serverError().build();
    }

    @POST
    @Path("/commit")
    public Response commit(@RestForm String message) throws IOException {
        if (Objects.nonNull(git)) {
            try {
                git.commit().setMessage(message).call();
                PushCommand pushCommand = git.push();
                pushCommand.setCredentialsProvider(user);
                pushCommand.setRemote("origin").call();
                LOGGER.info("committed message = " + message);
                return Response.ok().build();
            } catch (GitAPIException e) {
                LOGGER.error(e);
            }
        }
        return Response.serverError().build();
    }

    @DELETE
    public Response rm(@RestForm String filepattern) throws IOException {
        if (Objects.nonNull(git)) {
            try {
                git.rm().addFilepattern(filepattern).call();
                LOGGER.info("rm file from added by filepattern = " + filepattern);
                return Response.ok().build();
            } catch (GitAPIException e) {
                LOGGER.error(e);
            }
        }
        return Response.serverError().build();
    }

    private boolean checkRemoteExist() {
        final LsRemoteCommand lsremoteCommand = Git.lsRemoteRepository();
        try {
            lsremoteCommand.setRemote(remoteURL).setCredentialsProvider(user).call();
        } catch (GitAPIException e) {
            return false;
        }
        return true;
    }

    private Git pullRepository(final File workingDir) throws GitAPIException, IOException {
        final Git git = Git.open(workingDir);
        git.pull().setRebase(true)
                .setCredentialsProvider(user)
                .call();
        return git;
    }

    private Git cloneRepository(final File workingDir) throws GitAPIException {
        final CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(remoteURL)
                .setDirectory(workingDir)
                .setCredentialsProvider(user);
        return cloneCommand.call();
    }

    private void initializeRepository() {
        final File workingDir = workspaceProvider.getRootDirectory();
        final File gitDirectory = new File(workingDir, ".git");
        LOGGER.info(
            "Initializing repository "
                    + remoteURL
                    + " in working dir "
                    + workingDir.getAbsolutePath());
        
         workspaceProvider.synchronizedOperation(
            new Callable<Void>() {
                @Override
                public Void call() {
                    try {
                        if (!gitDirectory.exists()) {
                            if (checkRemoteExist()) {
                                LOGGER.info("Local repository not found, creating a new clone...");
                                git = cloneRepository(workingDir);
                            } else {
                                LOGGER.error("Remote repository not found...");
                            }
                        } else {
                            try {
                                if (checkRemoteExist()) {
                                    LOGGER.info(
                                    "Existing local repository found, pulling latest changes...");
                                    git = pullRepository(workingDir);
                                }else {
                                    LOGGER.error("Remote repository not found...");
                                }
                            } catch (final Exception e) {
                                LOGGER.error(
                                        "Could not update existing local repository...",
                                        e);
                            }
                        }
                    } catch (final GitAPIException e) {
                        LOGGER.error("Unable to clone git repository at " + remoteURL, e);
                    }
                    return null;
                }
            });
    }
	
}
