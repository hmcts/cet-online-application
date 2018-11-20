package uk.gov.hmcts.reform.cet.document;

import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class DocumentGenerator {

    private static final String S2S_AUTH_TOKEN = "some-random-auth-token";

    @Value("${pdf-service.writ-template-location}")
    private String writTemplateLocation;

    @Value("${pdf-service.url}")
    private String pdfServiceUrl;

    public byte[] generateWritDocument(Map<String, Object> templateData) {
        try {
            File templateFile = ResourceUtils.getFile(writTemplateLocation);
            return PDFServiceClient.builder().build(() -> S2S_AUTH_TOKEN, new URI(pdfServiceUrl))
                    .generateFromHtml(Files.readAllBytes(templateFile.toPath()), templateData);
        } catch (URISyntaxException | IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }


}
