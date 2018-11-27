package uk.gov.hmcts.reform.cet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.URI;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {


    private String feeApiUrl = "http://localhost:4411/fees-register/fees/lookup";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Fee getFee(String event) {

        log.debug("Getting fee from: " + feeApiUrl);

        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", "miscellaneous")
                .queryParam("jurisdiction1", "civil")
                .queryParam("jurisdiction2", "high court")
                .queryParam("service", "civil money claims")
                .build().encode().toUri();

        ResponseEntity<Fee> feeResponse = restTemplate.getForEntity(uri, Fee.class);

        return feeResponse.getBody();
    }
}