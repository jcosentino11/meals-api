package me.josephcosentino.controller;

import me.josephcosentino.exception.NotFoundException;
import me.josephcosentino.model.Group;
import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import me.josephcosentino.repository.GroupRepository;
import me.josephcosentino.repository.UserRepository;
import me.josephcosentino.service.GroupService;
import me.josephcosentino.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(secure = false)
@EnableSpringDataWebSupport
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    public void getAllUsers() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("test-group");

        Set<Group> groups = new HashSet<>();
        groups.add(group);

        Role role = new Role();
        role.setId(1L);
        role.setValue("test-role");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(1L);
        user.setUsername("test-user");
        user.setPassword("pwd");
        user.setGroups(groups);
        user.setRoles(roles);

        List<User> users = new ArrayList<>();
        users.add(user);

        given(userService.findAll(Matchers.any(Pageable.class))).willReturn(users);

        mvc.perform(
                get("/api/v1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("test-user")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].groups[0].id", is(1)))
                .andExpect(jsonPath("$[0].groups[0].name", is("test-group")))
                .andExpect(jsonPath("$[0].roles[0].id", is(1)))
                .andExpect(jsonPath("$[0].roles[0].value", is("test-role")))
                .andExpect(jsonPath("$[0].authorities").doesNotExist())
                .andExpect(jsonPath("$[0].plannedMeals").doesNotExist())
                .andExpect(jsonPath("$[0].accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$[0].accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$[0].credentialsNonExpired").doesNotExist())
                .andExpect(jsonPath("$[0].enabled").doesNotExist());
    }

    @Test
    public void getUserById() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("test-group");

        Set<Group> groups = new HashSet<>();
        groups.add(group);

        Role role = new Role();
        role.setId(1L);
        role.setValue("test-role");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(1L);
        user.setUsername("test-user");
        user.setPassword("pwd");
        user.setGroups(groups);
        user.setRoles(roles);

        given(userService.findById(1L)).willReturn(user);

        mvc.perform(
                get("/api/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("test-user")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.groups[0].id", is(1)))
                .andExpect(jsonPath("$.groups[0].name", is("test-group")))
                .andExpect(jsonPath("$.roles[0].id", is(1)))
                .andExpect(jsonPath("$.roles[0].value", is("test-role")))
                .andExpect(jsonPath("$.authorities").doesNotExist())
                .andExpect(jsonPath("$.plannedMeals").doesNotExist())
                .andExpect(jsonPath("$.accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$.accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$.credentialsNonExpired").doesNotExist())
                .andExpect(jsonPath("$.enabled").doesNotExist());
    }

    @Test
    public void getNonExistentUserById() throws Exception {
        given(userService.findById(2L)).willThrow(new NotFoundException());
        mvc.perform(
                get("/api/v1/users/2")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void clearCache() throws Exception {
        mvc.perform(
                delete("/api/v1/users/cache"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void clearCacheById() throws Exception {
        mvc.perform(
                delete("/api/v1/users/1/cache"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

}
