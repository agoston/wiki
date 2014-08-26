package org.bruinproductions.wiki;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WikiMain.class})
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WikiIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseURL = "http://localhost:8080";

    @Test
    public void testGet() {
        WikiDoc document = restTemplate.getForObject(baseURL + "/wiki/Latest_plane_crash", WikiDoc.class);
        assertThat(document.getDocument(), is(5702887));
    }

}
