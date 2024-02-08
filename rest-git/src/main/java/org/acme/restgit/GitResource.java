package org.acme.restgit;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.logging.Logger;

import com.google.common.io.Files;

@Path("/git")
public class GitResource {

    private static final String GIT_DIR = "/var/git/myrepo/";
    private static final Logger LOGGER = Logger.getLogger(GitResource.class.getName());

    @POST
    public Response add(@RestForm String description, @RestForm("file") FileUpload file) throws IOException {
        LOGGER.info("filename = " + file.fileName());
        Git git = openOrCreate( new File(GIT_DIR + ".git"));
        Files.move(file.uploadedFile().toFile(), new File(GIT_DIR + file.fileName()));
        try {
            git.add().addFilepattern(".").call();
        } catch (GitAPIException e) {
            LOGGER.error(e);
        }finally{
            git.close();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/commit")
    public Response commit(@RestForm String message) throws IOException {
        LOGGER.info("commit message = " + message);
        Git git = openOrCreate( new File(GIT_DIR + ".git"));
        addRemoteRepo(git);
        try {
            git.commit().setMessage(message).call();
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("root", "P@ssw0rd"));
            pushCommand.setRemote("origin").call();
        } catch (GitAPIException e) {
            LOGGER.error(e);
        }
        return Response.ok().build();
    }

    @DELETE
    public Response rm(@RestForm String filepattern) throws IOException {
        LOGGER.info("rm filepattern = " + filepattern);
        Git git = openOrCreate( new File(GIT_DIR + ".git"));
        try {
            git.rm().addFilepattern(filepattern).call();
        } catch (GitAPIException e) {
            LOGGER.error(e);
        }finally{
            git.close();
        }
        return Response.ok().build();
    }

    private static Git openOrCreate(File gitDirectory) throws IOException {
        Repository repository = new FileRepository(gitDirectory);
        try {
          repository.create();
        } catch(IllegalStateException repositoryExists) {
        }
        return new Git(repository);
    }

    private static void addRemoteRepo(Git git) throws IOException {
        try {
            git.remoteAdd().setName("origin").setUri(new URIish("http://gitlab-local:8000/root/myrepo.git")).call();
        } catch (GitAPIException e) {
            LOGGER.error(e);
        } catch (URISyntaxException e){
            LOGGER.error(e);
        }
    }
}
