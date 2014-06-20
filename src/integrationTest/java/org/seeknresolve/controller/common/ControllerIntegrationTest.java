package org.seeknresolve.controller.common;

import org.seeknresolve.TestAppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@SpringApplicationConfiguration(classes = TestAppContext.class)
@ActiveProfiles("test")
public class ControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }
}
