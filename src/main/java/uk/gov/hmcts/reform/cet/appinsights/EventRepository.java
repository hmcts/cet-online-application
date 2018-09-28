package uk.gov.hmcts.reform.cet.appinsights;

import java.util.Map;

public interface EventRepository {

    void trackEvent(String name, Map<String, String> properties);
}
