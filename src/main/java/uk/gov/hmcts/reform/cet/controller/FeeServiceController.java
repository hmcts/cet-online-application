package uk.gov.hmcts.reform.cet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.cet.config.FeeServiceConfiguration;
import uk.gov.hmcts.reform.cet.model.Fee;
import uk.gov.hmcts.reform.cet.service.FeePaymentService;
import uk.gov.hmcts.reform.cet.service.FeePaymentServiceClient;

@RestController
@RequestMapping("/fee")
public class FeeServiceController {

    private FeePaymentServiceClient feePaymentServiceClient;

    @Autowired
    private FeeServiceConfiguration configuration;

    @Autowired
    private FeePaymentService feePaymentService;

    @RequestMapping("")
    public String getFee() {
        Fee fee = feePaymentService.getFee();
        return fee.getFee_amount();
    }

    
}
