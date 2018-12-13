package uk.gov.hmcts.reform.cet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.hmcts.reform.cet.model.Fee;
import uk.gov.hmcts.reform.cet.service.FeePaymentService;
import uk.gov.hmcts.reform.cet.service.FeePaymentServiceClient;

@RestController
@RequestMapping("/civil-enforcement")
public class HelloController {

    @Autowired
    FeePaymentServiceClient client;

    @RequestMapping("")
    public Fee greet() {

        return client.getFee("civil money claims",
                "civil",
                "high court",
                "default",
                "miscellaneous"
                );
    }
    
}
