package uk.gov.hmcts.reform.cet.functional.bdd.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.cet.utils.JsonUtils;

public class IdamUtils {

    public static void createIdamUser() throws JsonProcessingException {
        ImmutableMap<String, Object> body = ImmutableMap.of(
                "email", "cet.citizen@hmcts.net",
                "password", "password",
                "forename", "Cet",
                "surname", "Citizen",
                "roles", ImmutableList.of(
                        ImmutableMap.of(
                                "code", "citizen"
                        )
                )
        );
        String json = JsonUtils.toJson(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setOutputStreaming(false);
        ResponseEntity<String> responseEntity = new RestTemplate()
                .postForEntity("http://localhost:4501/testing-support/accounts", request, String.class);
    }
}
