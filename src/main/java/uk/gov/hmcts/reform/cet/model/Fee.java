package uk.gov.hmcts.reform.cet.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fee {

    String code;

    String fee_amount;

    int  version;

    String description;
}
