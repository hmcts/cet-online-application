package uk.gov.hmcts.reform.cet.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.cet.config.FeeServiceConfiguration;
import uk.gov.hmcts.reform.cet.model.Fee;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class FeePaymentServiceImplTest {

    @Mock
    private FeeServiceConfiguration configuration;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FeePaymentServiceImpl feePaymentService;

    @Before
    public void setUp() {
        Fee fee = Fee.builder().code("foo").fee_amount("1").description("bar").version(1).build();
        ResponseEntity<Fee> responseEntity = new ResponseEntity<>(fee, HttpStatus.OK);
        when(restTemplate.getForEntity(any(URI.class), any(Class.class))).thenReturn(responseEntity);
        when(configuration.getUrl()).thenReturn("http://localhost");
        when(configuration.getApi()).thenReturn("/foo");
    }
    @Test
    public void testGetFee() {
        Fee fee = feePaymentService.getFee();
        assertEquals("foo", fee.getCode());
        assertEquals(1, fee.getVersion());
        assertEquals("bar", fee.getDescription());
    }

}