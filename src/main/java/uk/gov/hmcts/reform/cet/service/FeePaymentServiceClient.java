package uk.gov.hmcts.reform.cet.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.cet.model.Fee;

@FeignClient(name = "feeclient", url = "${fee.api.url}")
public interface FeePaymentServiceClient {

    @GetMapping(value = "/fees-register/fees/lookup")
    Fee getFee(@RequestParam("service") String service,
               @RequestParam("jurisdiction1") String jurisdiction1,
               @RequestParam("jurisdiction2") String jurisdiction2,
               @RequestParam("channel") String channel,
               @RequestParam("event") String event);
}
