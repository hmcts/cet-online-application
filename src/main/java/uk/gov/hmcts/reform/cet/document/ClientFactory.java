package uk.gov.hmcts.reform.cet.document;

import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ClientFactory.class);
    private static final String S2S_AUTH_TOKEN = "some-random-auth-token";

    @Value("${pdf-service.url}")
    private String pdfServiceUrl;

    public PDFServiceClient createPdfServiceClient() {
        try {
            return PDFServiceClient.builder().build(() -> S2S_AUTH_TOKEN, new URI(pdfServiceUrl));
        } catch (URISyntaxException exception) {
            LOG.error("There was a problem generating the writ document", exception);
            return null;
        }
    }
}
