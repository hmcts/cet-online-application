package uk.gov.hmcts.reform.cet.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("functional")
public class ApplicationTests {

    @Test
    public void sample_test() {
        assertThat(2).isGreaterThan(1);
    }

}
