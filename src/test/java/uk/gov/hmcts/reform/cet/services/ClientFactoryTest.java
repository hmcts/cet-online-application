package uk.gov.hmcts.reform.cet.services;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;
import uk.gov.service.notify.NotificationClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DocumentGenerator.class, ClientFactory.class})
@TestPropertySource(properties = {
        "pdf-service.url=http://localhost",
        "gov-notify.api-key=some-random-api-key-1234"
})
public class ClientFactoryTest {

    @Value("${gov-notify.api-key}")
    private String apiKey;

    @Autowired
    private ClientFactory clientFactory;


    @Test
    public void shouldCreatePDFServiceClient() {
        PDFServiceClient client = clientFactory.createPdfServiceClient();

        assertThat(client, isA(PDFServiceClient.class));
    }

    @Test
    public void shouldThrowExceptionGivenInvalidURI() {
        ReflectionTestUtils.setField(clientFactory, "pdfServiceUrl", "${invalid-url}");

        PDFServiceClient client = clientFactory.createPdfServiceClient();

        assertThat(client, is(nullValue()));
    }


    @Test
    public void shouldCreateNotificationClient() {
        NotificationClient client = clientFactory.createNotificationClient();

        assertThat(client, isA(NotificationClient.class));
        assertEquals(apiKey, client.getApiKey());
    }
}