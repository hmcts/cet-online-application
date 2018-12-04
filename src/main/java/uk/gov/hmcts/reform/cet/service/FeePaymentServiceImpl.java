package uk.gov.hmcts.reform.cet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.cet.config.FeeServiceConfiguration;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.URI;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {

    @Autowired
    private FeeServiceConfiguration configuration;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Fee getFee() {
        String feeApiUrl = configuration.getUrl() + configuration.getApi();

        log.debug("Getting fee from: " + feeApiUrl);

        URI uri = UriComponentsBuilder.fromHttpUrl(configuration.getUrl() + configuration.getApi())
                .queryParam("channel", configuration.getChannel())
                .queryParam("event", configuration.getEvent())
                .queryParam("jurisdiction1", configuration.getJurisdiction1())
                .queryParam("jurisdiction2", configuration.getJurisdiction2())
                .queryParam("service", configuration.getService())
                .build().encode().toUri();

        ResponseEntity<Fee> feeResponse = restTemplate.getForEntity(uri, Fee.class);

        return feeResponse.getBody();
    }
}