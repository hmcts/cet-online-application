package uk.gov.hmcts.reform.cet.functional;

import static org.junit.Assert.assertNotNull;

import uk.gov.hmcts.reform.cet.document.ClientFactory;
import uk.gov.hmcts.reform.cet.document.DocumentGenerator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DocumentGenerator.class, ClientFactory.class})
public class DocumentGeneratorTest {

    @Autowired
    private DocumentGenerator documentGenerator;

    @Test
    public void shouldGenerateDocumentFromTemplate() {

        Map<String, Object> templateData = new HashMap();
        templateData.put("claimantName", "Bob Smith");
        templateData.put("claimantAddress", "Eastside Street, London, UK");
        templateData.put("defendantName", "James White");
        templateData.put("defendantAddress", "Courtside House, North road, London, UK");
        templateData.put("hceoName", "Norman Taylor");
        templateData.put("totalAmount", "2456");
        templateData.put("issuingCourt", "Queen’s bench division");
        templateData.put("issueDate", "8 October 2018");
        templateData.put("judgementDate", "11-04-2017");
        templateData.put("judgementAmount", "3000");
        templateData.put("courtFees", "55");
        templateData.put("assessedCosts", "60");
        templateData.put("interestAmount", "341");
        templateData.put("amountPaid", "0");
        templateData.put("claimNumber", "D5FJ1003");
        templateData.put("claimDate", "11-01-2017");
        templateData.put("originalClaimant", "Mos Def");
        templateData.put("claimReason", "Dart Charge");
        templateData.put("applicantName", "Wilkin Chapman LLP");
        templateData.put("applicantAddress", "Cartergate House, 26 Chantry Lane, Grimsby, DN3 1LJ");


        byte[] pdfBytes = documentGenerator.generateWritDocument(templateData);

        assertNotNull(pdfBytes);
    }

}