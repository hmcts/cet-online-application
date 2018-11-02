package uk.gov.hmcts.reform.cet.functional.bdd.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TestContext {

    @Autowired
    private HttpContext httpContext;
}