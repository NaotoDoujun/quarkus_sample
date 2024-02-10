package org.acme.restbackend;

import java.util.Optional;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class MultiPartPayloadFormData { 

    @RestForm("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    FileUpload file;
  
    @RestForm("description")
    @PartType(MediaType.TEXT_PLAIN)
    Optional<String> description;

    public MultiPartPayloadFormData(FileUpload file, String description) {
        this.file = file;
        this.description = Optional.ofNullable(description);
    }

    public String getDescription() {
        return this.description.orElse("none");
    }

    public FileUpload getFile() {
        return this.file;
    }

}