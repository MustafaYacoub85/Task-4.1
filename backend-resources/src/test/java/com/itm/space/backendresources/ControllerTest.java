package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest extends BaseIntegrationTest {


    @MockBean
    UserService userService;

    @Test
    @WithMockUser(roles = "MODERATOR")  //проверяем, создает ли юзера от имени модератора
    public void createUser() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","anton","Anton","Antonov");
            mvc.perform(requestWithContent(post("/api/users"), userRequest))
                    .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles="MODERATOR") //проверяем поля username на отсутсвие символов
    public void userNameNotBlank () throws Exception {
        UserRequest userRequest = new UserRequest("","email@email.em","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="MODERATOR") //проверяем поля username на количество  символов
    public void userNameSizeMin() throws Exception {
        UserRequest userRequest = new UserRequest("n","email@email.em","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles="MODERATOR")//проверяем поля username на количество  символов
    public void userNameSizeMax() throws Exception{
        UserRequest userRequest =new UserRequest("namenamenamenamenamenamenamename","email@email.em","anton","Anton","Antonov");
    mvc.perform(requestWithContent(post("/api/users"), userRequest))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле email. оно не должно быть пустым.
    public void emailNotBlank() throws Exception {
        UserRequest userRequest = new UserRequest("name","","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле email. оно  должно быть в формате символы@символы.символы
    public void emailRegexp() throws Exception{
        UserRequest userRequest = new UserRequest("name","emailemailem","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле password. оно не должно быть пустым.
    public void passwordNotBlank() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле password. в нём не должно быть менее 4-х символов.
    public void passwordSizeMin() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","w","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле firstName. оно не должно быть пустым.
    public void firstNameNotBlank() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","","","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(roles = "MODERATOR") //проверяем поле lastName. оно не должно быть пустым.
    public void lastNameNotBlank() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","dfgdfg","Anton","");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "USER") //проверяем, можем ли создать юзера от имени USER.
    public void createUserWithUSER() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser //проверяем, можем ли создать юзера без аутинтификаций.
    public void createWithAnonymousUser() throws Exception {
        UserRequest userRequest = new UserRequest("name","email@email.em","anton","Anton","Antonov");
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isUnauthorized());

    }


    @Test
   @WithMockUser(roles = "MODERATOR") // проверяем случай когда пытаемся обратиться к не существующей конечной точке от имени "MODERATOR"
    public void testNotFoundEndPointMODERATOR() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/notFoundEndPoint"))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "USER") // проверяем случай когда пытаемся обратиться к не существующей конечной точке от имени "USER"
    public void testNotFoundEndPointUSER() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/notFoundEndPoint"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MODERATOR") //проверрряем информацию об пользователе по его ID от имени модератора
    public void getExistingUserByIdTestWithModerator() throws Exception {
        UUID uuid = UUID.fromString("a361c9b6-522a-4e57-85e7-e7f72a63ad6c");
        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + uuid))
                .andExpect(status().isOk());


    }

    @Test
    @WithMockUser(roles = "USER")  //проверрряем информацию об пользователе по его ID от имени юзера
    public void getExistingUserByIdTestWithUser() throws Exception {
        UUID uuid = UUID.fromString("a361c9b6-522a-4e57-85e7-e7f72a63ad6c");
        mvc.perform(MockMvcRequestBuilders.get("/api/users/" + uuid))
                .andExpect(status().isForbidden());

    }



    @Test
    @WithMockUser(roles = "MODERATOR") //проверка метода hello, который возвращает имя пользователя от имени модератора
    public void helloTestWithModerator() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER") //проверка метода hello, который возвращает имя пользователя от имени пользователя
    public void helloTestWithUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/users/hello"))
                .andExpect(status().isForbidden());
    }
}

