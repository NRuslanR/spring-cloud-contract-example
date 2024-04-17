package org.example.producer_http_rest.application;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(
    webEnvironment = WebEnvironment.MOCK,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
    }
)
@DirtiesContext
@AutoConfigureMessageVerifier
public class ProducerServiceContractTests 
{
    @Autowired
    private ApplicationController applicationController;

    @BeforeEach
    public void setup()
    {
        var mockMvcBuilder = 
            MockMvcBuilders
                .standaloneSetup(applicationController)
                .setControllerAdvice(new ApplicationExceptionHandler());

        RestAssuredMockMvc.standaloneSetup(mockMvcBuilder);
    }
}
