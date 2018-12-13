package uk.gov.hmcts.reform.cet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.cet.config.FeeServiceConfiguration;
import uk.gov.hmcts.reform.cet.model.Fee;
import uk.gov.hmcts.reform.cet.service.FeePaymentServiceClient;

@RestController
@RequestMapping("/fee")
public class FeeServiceController {

    @Autowired
    private FeeServiceConfiguration configuration;

    @Autowired
    private FeePaymentServiceClient client;

    @RequestMapping("")
    public String getFee() {
        Fee clientFee = client.getFee(configuration.getService(),
                configuration.getJurisdiction1(),
                configuration.getJurisdiction2(),
                configuration.getChannel(),
                configuration.getEvent());

        return clientFee.getFee_amount();
    }


}
