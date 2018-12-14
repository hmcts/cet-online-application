package uk.gov.hmcts.reform.cet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CaseDataServiceStubImpl implements CaseDataService {

    @Override
    public String getCaseData(String caseNumber) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode claimant = objectMapper.createObjectNode();
        claimant.set("firstName", new TextNode("Kate"));
        claimant.set("lastName", new TextNode("Montague"));
        claimant.set("address", new TextNode("123 Flower Pot Road. OX7 6QW"));

        ObjectNode defendant = objectMapper.createObjectNode();
        defendant.set("firstName", new TextNode("Andrew"));
        defendant.set("lastName", new TextNode("Lions"));
        defendant.set("address", new TextNode("4 Hampton Road, Oxford, OX4 4PL"));

        ObjectNode judgement = objectMapper.createObjectNode();
        judgement.set("date", new TextNode("11-12-2018"));
        judgement.set("award", new TextNode("3000"));
        judgement.set("courtFees", new TextNode("55"));
        judgement.set("assessedCosts", new TextNode("60"));
        judgement.set("interestAmount", new TextNode("341"));
        judgement.set("amountPaid", new TextNode("0"));

        ObjectNode claim = objectMapper.createObjectNode();
        claim.set("number", new TextNode("D5FJ1003"));
        claim.set("date", new TextNode("11-01-2017"));
        claim.set("originalClaimant", new TextNode("Mos Def"));
        claim.set("debtReason", new TextNode("Dart Charge"));
        claim.set("applicant", new TextNode("Wilkin Chapman LLP, Cartergate House, 26 Chantry Lane, Grimsby, DN3 1LJ"));

        ObjectNode formdata = objectMapper.createObjectNode();
        formdata.set("claimant", claimant);
        formdata.set("defendant", defendant);
        formdata.set("judgement", judgement);
        formdata.set("claim", claim);

        return objectMapper.createObjectNode()
                .set("formdata", formdata)
                .toString();
    }
}
