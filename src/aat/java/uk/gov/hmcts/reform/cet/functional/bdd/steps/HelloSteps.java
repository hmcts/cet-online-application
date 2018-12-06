package uk.gov.hmcts.reform.cet.functional.bdd.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.reform.cet.functional.bdd.utils.TestContext;

import static org.junit.Assert.assertEquals;

// Should refactor this when we have better requirements
@ContextConfiguration
@SpringBootTest
@ActiveProfiles("functional")
public class HelloSteps extends BaseSteps {

    @Autowired
    public HelloSteps(TestContext testContext) {
        super(testContext);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @When("^a (.*) request is sent to hello$")
    public void aRequestIsSentToHello(String method) {
        ResponseEntity response = sendRequest(baseUrl + "/civil-enforcement", method, "");
        testContext.getHttpContext().setResponseBodyAndStatesForResponse(response);
    }

    @When("^a (.*) request is sent to fee")
    public void aRequestIsSentToFee(String method) {
        ResponseEntity response = sendRequest(baseUrl + "/fee", method, "");
        testContext.getHttpContext().setResponseBodyAndStatesForResponse(response);
    }

    @Then("^the response code is (\\d+)$")
    public void the_response_code_is(int responseCode) {
        assertEquals("Response status code", responseCode, testContext.getHttpContext().getHttpResponseStatusCode());
    }

    @Then("^the fee is (.*+)$")
    public void the_response_code_is(String fee) {
        assertEquals("Content", fee, testContext.getHttpContext().getRawResponseString());
    }
}
