package ru.topjavacoursework.web.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.Role;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.service.UserService;
import ru.topjavacoursework.util.JsonUtil;
import ru.topjavacoursework.web.AbstractRestControllerTest;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjavacoursework.TestUtil.userHttpBasic;
import static ru.topjavacoursework.UserTestData.*;


public class UserAdminRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = UserAdminRestController.REST_URL + '/';

    @Autowired
    private UserService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(ADMIN));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testGetByEmail() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=" + ADMIN.getEmail())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(ADMIN));
    }

    @Test
    public void testGetByEmailNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "by?email=noemail@world.org")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + USER_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());
        MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN), service.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreate() throws Exception {
        User expected = getNew();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        User returned = MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());

        MATCHER.assertEquals(expected, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, expected, USER), service.getAll());
    }

    @Test
    public void testCreateInvalid() throws Exception {
        User expected = new User(null, null, null, "newPass", Role.USER, Role.ADMIN);
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{'cause':'ValidationException'}"))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testCreateDuplicate() throws Exception {
        User expected = new User(null, "New", "user@world.org", "newPass", Role.USER, Role.ADMIN);
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = getUpdated();
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertEquals(updated, service.get(USER_ID));
    }

    @Test
    public void testUpdateInvalid() throws Exception {
        User updated = new User(USER);
        updated.setName(null);
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().json("{'cause':'ValidationException'}"));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testUpdateDuplicate() throws Exception {
        User updated = new User(USER);
        updated.setEmail("admin@world.org");
        mockMvc.perform(put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(ADMIN, USER));
    }

}