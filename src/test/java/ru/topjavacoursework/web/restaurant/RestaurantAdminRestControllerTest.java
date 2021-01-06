package ru.topjavacoursework.web.restaurant;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.Restaurant;
import ru.topjavacoursework.service.RestaurantService;
import ru.topjavacoursework.util.JsonUtil;
import ru.topjavacoursework.web.AbstractRestControllerTest;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjavacoursework.RestaurantTestData.*;
import static ru.topjavacoursework.TestUtil.userHttpBasic;
import static ru.topjavacoursework.UserTestData.ADMIN;
import static ru.topjavacoursework.UserTestData.USER;


public class RestaurantAdminRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = RestaurantAdminRestController.REST_URL + "/";

    @Autowired
    private RestaurantService service;

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(REST1, REST2));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + REST1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(REST1));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
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
        Restaurant expected = getNew();
        ResultActions actions = mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        Restaurant saved = MATCHER.fromJsonAction(actions);
        expected.setId(saved.getId());

        MATCHER.assertEquals(expected, saved);
        MATCHER.assertCollectionEquals(Arrays.asList(expected, REST1, REST2), service.getAll());
    }

    @Test
    public void testCreateInvalid() throws Exception {
        Restaurant restaurant = new Restaurant();
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Ignore
    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testCreateDuplicate() throws Exception {
        Restaurant restaurant = new Restaurant(null, "Прага");
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(restaurant)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant expected = getUpdated();
        ResultActions actions = mockMvc.perform(put(REST_URL + REST1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertEquals(expected, service.get(REST1_ID));
    }

    @Test
    public void testUpdateInvalid() throws Exception {
        Restaurant expected = getUpdated();
        expected.setName(null);
        ResultActions actions = mockMvc.perform(put(REST_URL + REST1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Ignore
    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testUpdateDuplicate() throws Exception {
        Restaurant expected = REST2;
        expected.setName("Прага");
        ResultActions actions = mockMvc.perform(put(REST_URL + REST2_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + REST1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertCollectionEquals(Collections.singletonList(REST2), service.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}