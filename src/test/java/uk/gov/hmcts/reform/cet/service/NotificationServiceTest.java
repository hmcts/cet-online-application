package uk.gov.hmcts.reform.cet.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.cet.services.ClientFactory;
import uk.gov.hmcts.reform.cet.utils.JsonUtils;
import uk.gov.service.notify.*;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class NotificationServiceTest {

    @Mock
    private ClientFactory clientFactory;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    @Before
    public void setUp() throws Exception {
        String emailJsonResponse = JsonUtils.getJsonInput("util/SendEmailResponse");
        String letterJsonResponse = JsonUtils.getJsonInput("util/LetterResponse");
        String smsJsonResponse = JsonUtils.getJsonInput("util/SendSmsResponse");
        SendSmsResponse smsResponse = new SendSmsResponse(smsJsonResponse);
        LetterResponse letterResponse = new LetterResponse(letterJsonResponse);
        SendEmailResponse emailResponse = new SendEmailResponse(emailJsonResponse);
        when(clientFactory.createNotificationClient()).thenReturn(notificationClient);
        when(notificationClient.sendEmail(any(), anyString(), anyMap(), anyString())).thenReturn(emailResponse);
        when(notificationClient.sendPrecompiledLetter(any(), any())).thenReturn(letterResponse);
        when(notificationClient.sendSms(any(), anyString(), anyMap(), anyString())).thenReturn(smsResponse);
    }

    @Test
    public void testSendEmailNotification() throws Exception {
        // SendEmailResponse doesn't deserialize scheduled_for or uri fields
        SendEmailResponse response = notificationService.sendEmailNotification("foo@foo.com", "bar");
        assertEquals("Testing Civil Enforcement steel thread integration with Gov.Notify", response.getBody());
        assertEquals("civil.enforcement@notifications.service.gov.uk", response.getFromEmail().get());
        assertEquals("Civil Enforcement steel thread integration test", response.getSubject());
        assertEquals("04df41df-7989-43d0-9e31-dd229d50c997", response.getNotificationId().toString());
        assertEquals("cet-test-email-1234", response.getReference().get());
        assertEquals("0711d881-2fea-4fd0-8c7f-779d193bf965", response.getTemplateId().toString());
        assertEquals("https://api.notifications.service.gov.uk/services/4c2bd129-5222-4d6a-8161-ff95447ad4dd/templates/0711d881-2fea-4fd0-8c7f-779d193bf965", response.getTemplateUri());
        assertEquals(1, response.getTemplateVersion());
    }

    @Test
    public void testSendPrecompiledLetter() throws Exception {
        LetterResponse response = notificationService.sendLetter( new File(""), "foo");
        assertEquals("cet-test-letter-1234", response.getReference().get());
        assertEquals("0711d881-2fea-4fd0-8c7f-779d193bf965", response.getNotificationId().toString());
    }

    @Test
    public void testSendSms() throws Exception {
        // SendSmsResponse doesn't deserialize scheduled_for or uri fields
        SendSmsResponse response = notificationService.sendSms("foo", new HashMap(), "bar");
        assertEquals("Testing Civil Enforcement integration with Gov.Notify", response.getBody());
        assertEquals("GOVUK", response.getFromNumber().get());
        assertEquals("83f209a6-ceaa-4045-ae3e-931fa77b20fb", response.getNotificationId().toString());
        assertEquals("71eae03b-c0ee-4107-8d6d-d8b14a113909", response.getTemplateId().toString());
        assertEquals("https://api.notifications.service.gov.uk/services/4c2bd129-5222-4d6a-8161-ff95447ad4dd/templates/71eae03b-c0ee-4107-8d6d-d8b14a113909", response.getTemplateUri());
        assertEquals(1, response.getTemplateVersion());
    }
}