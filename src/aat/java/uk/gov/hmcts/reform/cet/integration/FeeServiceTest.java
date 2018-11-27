package uk.gov.hmcts.reform.cet.integration;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.URI;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@ContextConfiguration
public class FeeServiceTest {

    private String feeApiUrl = "http://localhost:4411/fees-register/fees/lookup";

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getFee() {

        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", "miscellaneous")
                .queryParam("jurisdiction1", "civil")
                .queryParam("jurisdiction2", "high court")
                .queryParam("service", "civil money claims")
                .build().encode().toUri();

        ResponseEntity<Fee> feeResponse = restTemplate.getForEntity(uri, Fee.class);
        Fee fee = feeResponse.getBody();
        assertEquals("FEE0011", fee.getCode());
        assertEquals("Appellant’s/respondent’s notice (High Court)", fee.getDescription());
    }
}