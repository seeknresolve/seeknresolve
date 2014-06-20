package org.seeknresolve.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seeknresolve.controller.common.ControllerIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerIntegrationTest extends ControllerIntegrationTest {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void shouldReturn404ForNonExistingUser() throws Exception {
        getMockMvc().perform(get("/user/details/NonExistingUser")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
