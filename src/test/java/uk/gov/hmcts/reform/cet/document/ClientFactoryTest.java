package uk.gov.hmcts.reform.cet.document;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DocumentGenerator.class, ClientFactory.class})
@TestPropertySource(properties = {
        "pdf-service.url=http://localhost"
})
public class ClientFactoryTest {

    @Autowired
    private ClientFactory clientFactory;


    @Test
    public void shouldCreatePDFServiceClient() {
        PDFServiceClient client = clientFactory.createPdfServiceClient();

        assertThat(client, isA(PDFServiceClient.class));
    }


    @Test
    public void shouldThrowExceptionWhenFileNotFound() {
        ReflectionTestUtils.setField(clientFactory, "pdfServiceUrl", "${invalid-url}");

        PDFServiceClient client = clientFactory.createPdfServiceClient();

        assertThat(client, is(nullValue()));
    }


}