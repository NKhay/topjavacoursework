package ru.topjavacoursework.web.dish;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.service.DishService;
import ru.topjavacoursework.to.DishTo;
import ru.topjavacoursework.util.JsonUtil;
import ru.topjavacoursework.web.AbstractRestControllerTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjavacoursework.DishTestData.*;
import static ru.topjavacoursework.RestaurantTestData.REST1_ID;
import static ru.topjavacoursework.RestaurantTestData.REST2_ID;
import static ru.topjavacoursework.TestUtil.userHttpBasic;
import static ru.topjavacoursework.UserTestData.ADMIN;
import static ru.topjavacoursework.UserTestData.USER;
import static ru.topjavacoursework.util.ModelUtil.asTo;
import static ru.topjavacoursework.util.ModelUtil.updateFromTo;


public class DishAdminRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_URL = DishAdminRestController.REST_URL.replace("{restaurantId}", String.valueOf(REST1_ID)) + "/";

    @Autowired
    private DishService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + DISH1_REST1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(DISH1_REST1));
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + DISH1_REST2_ID)
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
        DishTo expected = getNewTo();
        ResultActions actions = mockMvc.perform(post(DishAdminRestController.REST_URL.replace("{restaurantId}", String.valueOf(REST2_ID)))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        Dish saved = MATCHER.fromJsonAction(actions);
        expected.setId(saved.getId());
        saved = updateFromTo(saved, expected);

        MATCHER.assertCollectionEquals(Arrays.asList(saved, DISH1_REST2, DISH2_REST2), service.getAll(REST2_ID, LocalDate.now()));
    }

    @Test
    public void testCreateInvalid() throws Exception {
        DishTo expected = getNewTo();
        expected.setName(null);
        mockMvc.perform(post(DishAdminRestController.REST_URL.replace("{restaurantId}", String.valueOf(REST2_ID)))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Ignore
    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testCreateDuplicate() throws Exception {
        DishTo expected = asTo(DISH1_REST1);
        expected.setId(null);
        expected.setPrice(500);
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdate() throws Exception {
        DishTo expected = getUpdatedTo();
        mockMvc.perform(put(REST_URL + (DISH1_REST1_ID + 2))
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertEquals(updateFromTo(DISH3_REST1, expected), service.get(DISH1_REST1_ID + 2, REST1_ID));
    }

    @Test
    public void testUpdateInvalid() throws Exception {
        DishTo expected = getUpdatedTo();
        expected.setName(null);
        mockMvc.perform(put(REST_URL + (DISH1_REST1_ID + 2))
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
        DishTo expected = getUpdatedTo();
        expected.setName("Steak");
        mockMvc.perform(put(REST_URL + DISH3_REST1)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH1_REST1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk());

        MATCHER.assertCollectionEquals(Arrays.asList(DISH2_REST1, DISH3_REST1), service.getAll(REST1_ID, LocalDate.now()));
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + DISH1_REST2_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(REST1_DISHES));
    }

}