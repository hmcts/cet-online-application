package uk.gov.hmcts.reform.cet.service;

import uk.gov.hmcts.reform.cet.services.ClientFactory;
import uk.gov.service.notify.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${gov-notify.template-id.email.test}")
    private String emailTemplateId;

    @Value("${gov-notify.template-id.sms.test}")
    private String smsTemplateId;

    public SendEmailResponse sendEmailNotification(String emailAddress, String emailReference) throws NotificationClientException {
        LOG.info("sending email notification");
        return clientFactory.createNotificationClient()
                .sendEmail(emailTemplateId, emailAddress, new HashMap<>(), emailReference);
    }

    public LetterResponse sendLetter(File letter, String letterReference) throws NotificationClientException {
        LOG.info("sending letter notification");
        return clientFactory.createNotificationClient()
                .sendPrecompiledLetter(letterReference, letter);

    }

    public SendSmsResponse sendSms(String phoneNumber, Map<String, String> smsData, String reference) throws NotificationClientException {
        LOG.info("sending SMS notification");
        return clientFactory.createNotificationClient()
                .sendSms(smsTemplateId, phoneNumber, smsData, reference);

    }
}
