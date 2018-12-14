package uk.gov.hmcts.reform.cet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.cet.service.CaseDataService;

import java.io.IOException;

@RestController
@RequestMapping("/case-data")
public class CaseDataController {

    @Autowired
    private CaseDataService caseDataService;

    @RequestMapping("/{caseNumber}")
    public String getCaseData(@PathVariable String caseNumber) throws IOException {
        return caseDataService.getCaseData(caseNumber);
    }
}
