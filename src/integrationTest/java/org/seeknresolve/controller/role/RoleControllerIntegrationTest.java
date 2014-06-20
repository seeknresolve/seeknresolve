package org.seeknresolve.controller.role;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seeknresolve.controller.common.ControllerIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleControllerIntegrationTest extends ControllerIntegrationTest {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void shouldReturn404ForNonExistingRole() throws Exception {
        getMockMvc().perform(get("/role/details/NonExistingRole")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }
}
