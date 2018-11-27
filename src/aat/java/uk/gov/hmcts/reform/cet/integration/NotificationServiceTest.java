package uk.gov.hmcts.reform.cet.integration;

import static org.junit.Assert.assertEquals;
import static uk.gov.hmcts.reform.cet.integration.DocumentGeneratorTest.getTemplateData;

import uk.gov.hmcts.reform.cet.services.ClientFactory;
import uk.gov.hmcts.reform.cet.services.DocumentGenerator;
import uk.gov.hmcts.reform.cet.services.NotificationService;
import uk.gov.service.notify.LetterResponse;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;
import uk.gov.service.notify.SendSmsResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NotificationService.class, DocumentGenerator.class, ClientFactory.class})
public class NotificationServiceTest {

    @Autowired
    private DocumentGenerator documentGenerator;
    @Autowired
    private NotificationService notificationService;

    @Test
    public void shouldSendEmailToRecipient() throws NotificationClientException {
        SendEmailResponse sendEmailResponse = notificationService
                .sendEmailNotification("civil.enforcement@outlook.com", "cet-test-email-1234");

        assertEquals("cet-test-email-1234", sendEmailResponse.getReference().get());
    }

    @Test
    public void shouldSendSmsToRecipient() throws NotificationClientException {
        SendSmsResponse sendSmsResponse = notificationService
                .sendSms("07863445692", new HashMap<>(), "cet-test-sms-1234");

        assertEquals("cet-test-sms-1234", sendSmsResponse.getReference().get());
    }

    @Test
    public void shouldSendGeneratedDocumentToRecipient() throws NotificationClientException, IOException {
        byte[] pdfBytes = documentGenerator.generateWritDocument(getTemplateData());

        File generatedFile = new File("generatedPDF");
        Files.write(generatedFile.toPath(), pdfBytes);

        LetterResponse letterResponse = notificationService
                .sendLetter(generatedFile, "cet-test-letter-1234");

        assertEquals("cet-test-letter-1234", letterResponse.getReference().get());
    }
}