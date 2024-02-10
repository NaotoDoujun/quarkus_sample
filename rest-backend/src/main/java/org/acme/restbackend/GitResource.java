package org.acme.restbackend;

import java.io.IOException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.jboss.resteasy.reactive.client.api.ClientMultipartForm;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/git")
public class GitResource {
 
    @RestClient
    GitService gitService;

    private static final Logger LOGGER = Logger.getLogger(GitResource.class.getName());

    @POST
    public Response add(@RestForm String description, @RestForm("file") FileUpload file) throws IOException {
        LOGGER.info("filename = " + file.fileName());
        MultiPartPayloadFormData inputForm = new MultiPartPayloadFormData(file, description);
        gitService.add(buildClientMultipartForm(inputForm));
        return Response.ok().build();
    }

    @POST
    @Path("/commit")
    public Response commit(@RestForm String message) {
        LOGGER.info("message = " + message);
        gitService.commit(message);
        return Response.ok().build();
    }

    private ClientMultipartForm buildClientMultipartForm(MultiPartPayloadFormData inputForm) throws IOException { 
        ClientMultipartForm multiPartForm = ClientMultipartForm.create();
        multiPartForm.attribute("description", inputForm.getDescription(), "description");
        multiPartForm.binaryFileUpload("file", inputForm.getFile().fileName(), inputForm.getFile().filePath().toString(), inputForm.getFile().contentType());
        return multiPartForm;
    }
}
