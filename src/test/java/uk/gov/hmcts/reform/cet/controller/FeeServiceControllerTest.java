package uk.gov.hmcts.reform.cet.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.hmcts.reform.cet.config.FeeServiceConfiguration;
import uk.gov.hmcts.reform.cet.model.Fee;
import uk.gov.hmcts.reform.cet.service.FeePaymentServiceClient;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {HelloController.class}, secure = false)
public class FeeServiceControllerTest {

    @Mock
    private FeeServiceConfiguration configuration;

    @Mock
    private FeePaymentServiceClient client;

    @InjectMocks
    private FeeServiceController controller;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        Fee fee = Fee.builder().fee_amount("100").code("foo").description("bar").version(1).build();
        when(client.getFee("service", "jurisdiction1", "jurisdiction2", "channel", "event")).thenReturn(fee);
        when(configuration.getApi()).thenReturn("/api");
        when(configuration.getUrl()).thenReturn("www.google.com");
        when(configuration.getService()).thenReturn("service");
        when(configuration.getJurisdiction1()).thenReturn("jurisdiction1");
        when(configuration.getJurisdiction2()).thenReturn("jurisdiction2");
        when(configuration.getChannel()).thenReturn("channel");
        when(configuration.getEvent()).thenReturn("event");
    }

    @Test
    public void getFee() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/fee").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("100")));
    }
}
