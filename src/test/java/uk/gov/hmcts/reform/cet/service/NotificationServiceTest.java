package uk.gov.hmcts.reform.cet.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.cet.services.ClientFactory;
import uk.gov.hmcts.reform.cet.utils.JsonUtils;
import uk.gov.service.notify.NotificationClient;
import uk.gov.service.notify.SendEmailResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        String jsonResponse = JsonUtils.getJsonInput("util/SendEmailResponse");
        SendEmailResponse response = new SendEmailResponse(jsonResponse);
        when(clientFactory.createNotificationClient()).thenReturn(notificationClient);
        when(notificationClient.sendEmail(any(), any(), any(), any())).thenReturn(response);
    }

    @Test
    public void testSendEmailNotification() throws Exception {
        // Note that SendEmailResponse deserialize scheduled_for or uri fields
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
}