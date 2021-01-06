package ru.topjavacoursework.web;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.service.UserService;
import ru.topjavacoursework.to.UserTo;
import ru.topjavacoursework.util.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjavacoursework.UserTestData.*;
import static ru.topjavacoursework.util.ModelUtil.asTo;
import static ru.topjavacoursework.util.ModelUtil.updateFromTo;
import static ru.topjavacoursework.web.RootRestController.REST_URL;


public class RootRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private UserService service;

    @Test
    public void testRegister() throws Exception {
        UserTo expected = asTo(getNew());
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        User returned = MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());
        returned = updateFromTo(returned, expected);

        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, returned, USER), service.getAll());
    }

    @Test
    public void testRegisterInvalid() throws Exception {
        UserTo expected = new UserTo(null, null, null, "newPass");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'cause':'ValidationException'}"))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testRegisterDuplicate() throws Exception {
        UserTo expected = new UserTo(null, "New", "user@world.org", "newPass");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

}