package org.zalando.apifirst.example;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.zalando.model.User;

/**
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ExampleApplication.class)
@WebIntegrationTest(randomPort = true)
public class ApiFirstIT {

    @Value("${local.server.port}")
    private int port;

    private Client client;

    @Before
    public void setUp() {
        client = ClientBuilder.newClient();
    }

    @Test
    public void runApplication() {
        String username = "klaus";

        //
        WebTarget target = client.target("http://localhost:" + port).path("v2/user/" + username);
        User user = target.request(MediaType.APPLICATION_JSON_TYPE).get(User.class);

        //
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }
}
