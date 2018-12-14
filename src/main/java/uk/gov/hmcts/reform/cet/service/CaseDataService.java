package uk.gov.hmcts.reform.cet.service;

import java.io.IOException;

public interface CaseDataService {

    String getCaseData(String caseNumber) throws IOException;
}
