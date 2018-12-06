package uk.gov.hmcts.reform.cet.functional;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;

import static org.junit.Assert.assertEquals;

@Configuration
@ComponentScan("uk.gov.hmcts.reform.cet.functional")
@PropertySource("application.properties")
public class FeeServiceTest {

    private String feeApiUrl = "http://localhost:8080/fee";
//    private String feeApiUrl = "http://fees-register-api-aat.service.core-compute-aat.internal/fees-register/fees/lookup";

    @Test
    public void getFee() {

        System.out.println("Sending request to: " + feeApiUrl);
        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", "miscellaneous")
                .queryParam("jurisdiction1", "civil")
                .queryParam("jurisdiction2", "high court")
                .queryParam("service", "civil money claims")
                .build().encode().toUri();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyout.reform.hmcts.net", 8080));
//        requestFactory.setProxy(proxy);
        HttpHeaders headers = HttpHeaders.EMPTY;
        headers.add("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NDZodmEyMHFoMHNuZnYyYmhiNzl2aXZmMSIsInN1YiI6IjI1IiwiaWF0IjoxNTQ0MDk1NjIxLCJleHAiOjE1NDQxMTM2MjEsImRhdGEiOiJjYXNld29ya2VyLWNlLGNhc2V3b3JrZXItY2UtbG9hMCIsInR5cGUiOiJBQ0NFU1MiLCJpZCI6IjI1IiwiZm9yZW5hbWUiOiJJbnRlZ3JhdGlvbiIsInN1cm5hbWUiOiJUZXN0IiwiZGVmYXVsdC1zZXJ2aWNlIjoiUHJvYmF0ZSIsImxvYSI6MCwiZGVmYXVsdC11cmwiOiJodHRwczovL2xvY2FsaG9zdDo5MDAwL3BvYy9wcm9iYXRlIiwiZ3JvdXAiOiJwcm9iYXRlLXByaXZhdGUtYmV0YSJ9.YAkG7QJ--tjBCNQ7aR1qqpRGcuVSSj4ZRL4qLvbiI1c");
        HttpEntity<String> request = new HttpEntity<>("", headers);
        RestTemplate restTemplate = new RestTemplate(requestFactory);


        ResponseEntity<Fee> feeResponse = restTemplate.exchange(feeApiUrl, HttpMethod.GET, request, Fee.class);
        Fee fee = feeResponse.getBody();
        assertEquals("FEE0444", fee.getCode());
        assertEquals("Appellants/respondents notice (High Court)", fee.getDescription());
    }
}