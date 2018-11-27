package uk.gov.hmcts.reform.cet.functional;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@ContextConfiguration
public class FeeServiceTest {

    @Value("${fee.api.url}")
    private String feeApiHostname;

    private String feeApiUrl = "http://fees-register-api-aat.service.core-compute-aat.internal/fees-register/fees/lookup";


    @Test
    public void getFee() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyout.reform.hmcts.net", 8080));
        requestFactory.setProxy(proxy);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", "miscellaneous")
                .queryParam("jurisdiction1", "civil")
                .queryParam("jurisdiction2", "high court")
                .queryParam("service", "civil money claims")
                .build().encode().toUri();

        ResponseEntity<Fee> feeResponse = restTemplate.getForEntity(uri, Fee.class);
        Fee fee = feeResponse.getBody();
        assertEquals("FEE0444", fee.getCode());
        assertEquals("Appellants/respondents notice (High Court)", fee.getDescription());
    }
}