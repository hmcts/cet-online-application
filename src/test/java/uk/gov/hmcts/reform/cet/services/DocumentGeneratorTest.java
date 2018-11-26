package uk.gov.hmcts.reform.cet.services;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import uk.gov.hmcts.reform.pdf.service.client.PDFServiceClient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DocumentGenerator.class, ClientFactory.class})
@TestPropertySource(properties = {
        "pdf-service.writ-template-location=classpath:templates/writ-control-example-template.html"
})
public class DocumentGeneratorTest {

    @MockBean
    private ClientFactory clientFactory;

    @MockBean
    private PDFServiceClient pdfServiceClient;

    @Autowired
    private DocumentGenerator documentGenerator;


    @Test
    public void shouldGenerateWritDocument() {
        byte[] expectedDocumentBytes = "test bytes".getBytes();
        when(clientFactory.createPdfServiceClient()).thenReturn(pdfServiceClient);
        doReturn(expectedDocumentBytes).when(pdfServiceClient).generateFromHtml(any(), any());
        final Map<String, Object> templateData = new HashMap<>();

        byte[] actualDocumentBytes = documentGenerator.generateWritDocument(templateData);

        assertThat(actualDocumentBytes, is(expectedDocumentBytes));
    }


    @Test
    public void shouldThrowExceptionWhenFileNotFound() {
        ReflectionTestUtils.setField(documentGenerator, "writTemplateLocation", "invalid-location");
        final Map<String, Object> templateData = new HashMap<>();

        byte[] actualDocumentBytes = documentGenerator.generateWritDocument(templateData);

        assertThat(actualDocumentBytes, is(nullValue()));
    }


}