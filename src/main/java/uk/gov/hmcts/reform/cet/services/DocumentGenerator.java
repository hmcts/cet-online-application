package uk.gov.hmcts.reform.cet.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class DocumentGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentGenerator.class);

    @Autowired
    private ClientFactory clientFactory;

    @Value("${pdf-service.writ-template-location}")
    private String writTemplateLocation;

    public byte[] generateWritDocument(Map<String, Object> templateData) {
        try {
            File templateFile = ResourceUtils.getFile(writTemplateLocation);
            return clientFactory.createPdfServiceClient()
                    .generateFromHtml(Files.readAllBytes(templateFile.toPath()), templateData);
        } catch (IOException exception) {
            LOG.error("There was a problem generating the writ document", exception);
        }
        return null;
    }
}
