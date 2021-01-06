package ru.topjavacoursework.web.dish;

import org.junit.Test;
import org.springframework.http.MediaType;
import ru.topjavacoursework.util.JsonUtil;
import ru.topjavacoursework.web.AbstractRestControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjavacoursework.DishTestData.*;
import static ru.topjavacoursework.RestaurantTestData.REST1_ID;
import static ru.topjavacoursework.TestUtil.userHttpBasic;
import static ru.topjavacoursework.UserTestData.USER;


public class DishRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_URL = DishRestController.REST_URL.replace("{restaurantId}", String.valueOf(REST1_ID)) + "/";

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentListMatcher(REST1_DISHES));
    }

    @Test
    public void testUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostNotAllowed() throws Exception {
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNewTo())))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

}