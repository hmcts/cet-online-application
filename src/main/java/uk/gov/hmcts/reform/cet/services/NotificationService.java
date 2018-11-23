package uk.gov.hmcts.reform.cet.services;

import uk.gov.service.notify.NotificationClientException;
import uk.gov.service.notify.SendEmailResponse;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private ClientFactory clientFactory;

    @Value("${gov-notify.api-key}")
    private String apiKey;

    @Value("${gov-notify.template-id.test}")
    private String templateId;

    public SendEmailResponse sendEmailNotification(String emailReference) throws NotificationClientException {
        LOG.info("sending email notification");
        return clientFactory.createNotificationClient()
                .sendEmail(templateId, "taleb.benouaer@hmcts.net", new HashMap<>(), emailReference);

    }
}
