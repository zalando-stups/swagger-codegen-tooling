package org.zalando.stups.jetty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SwaggerCodegenJettyApplication.class)
@WebIntegrationTest
public class SwaggerCodegenJettyApplicationTests {

    private final Logger log = LoggerFactory.getLogger(SwaggerCodegenJettyApplicationTests.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void contextLoads() {
        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject("http://localhost:" + port + "/v2/api-docs", String.class);
        log.info(response);
    }

}
