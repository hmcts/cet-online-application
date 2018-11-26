package uk.gov.hmcts.reform.cet.functional;

import static org.junit.Assert.assertEquals;

import uk.gov.hmcts.reform.cet.services.ClientFactory;
import uk.gov.hmcts.reform.cet.services.NotificationService;
import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {NotificationService.class, ClientFactory.class})
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void shouldSendEmailToRecipient() throws NotificationClientException {
        SendEmailResponse sendEmailResponse = notificationService
                .sendEmailNotification("civil.enforcement@outlook.com", "cet-test-email-1234");

        assertEquals("cet-test-email-1234", sendEmailResponse.getReference().get());
    }
}