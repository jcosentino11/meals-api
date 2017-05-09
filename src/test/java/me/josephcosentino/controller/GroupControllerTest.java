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
@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(secure = false)
@EnableSpringDataWebSupport
public class GroupControllerTest {

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
    public void getAllGroups() throws Exception {
        Group group1 = new Group();
        group1.setId(1L);
        group1.setName("test-group");

        Group group2 = new Group();
        group2.setId(2L);
        group2.setName("test-group-2");

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);

        given(groupService.findAll(Matchers.any(Pageable.class))).willReturn(groups);

        mvc.perform(
                get("/api/v1/groups")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("test-group")))
                .andExpect(jsonPath("$[0].users").doesNotExist())
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("test-group-2")))
                .andExpect(jsonPath("$[1].users").doesNotExist());
    }

    @Test
    public void getGroupById() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("test-group");

        given(groupService.findById(1L)).willReturn(group);

        mvc.perform(
                get("/api/v1/groups/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test-group")))
                .andExpect(jsonPath("$.users").doesNotExist());
    }

    @Test
    public void getNonExistentUserById() throws Exception {
        given(groupService.findById(2L)).willThrow(new NotFoundException());
        mvc.perform(
                get("/api/v1/groups/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUsersByGroupId() throws Exception {
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

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test-user");
        user1.setPassword("pwd");
        user1.setGroups(groups);
        user1.setRoles(roles);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test-user-2");
        user2.setPassword("pwd");
        user2.setGroups(groups);
        user2.setRoles(roles);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        given(userService.findByGroupId(1L)).willReturn(users);

        mvc.perform(
                get("/api/v1/groups/1/users")
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
                .andExpect(jsonPath("$[0].enabled").doesNotExist())
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("test-user-2")))
                .andExpect(jsonPath("$[1].password").doesNotExist())
                .andExpect(jsonPath("$[1].groups[0].id", is(1)))
                .andExpect(jsonPath("$[1].groups[0].name", is("test-group")))
                .andExpect(jsonPath("$[1].roles[0].id", is(1)))
                .andExpect(jsonPath("$[1].roles[0].value", is("test-role")))
                .andExpect(jsonPath("$[1].authorities").doesNotExist())
                .andExpect(jsonPath("$[1].plannedMeals").doesNotExist())
                .andExpect(jsonPath("$[1].accountNonExpired").doesNotExist())
                .andExpect(jsonPath("$[1].accountNonLocked").doesNotExist())
                .andExpect(jsonPath("$[1].credentialsNonExpired").doesNotExist())
                .andExpect(jsonPath("$[1].enabled").doesNotExist());
    }

    @Test
    public void getUsersByNonExistentGroupId() throws Exception {
        given(userService.findByGroupId(2L)).willThrow(new NotFoundException());
        mvc.perform(
                get("/api/v1/groups/2/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getNonExistentUsersByGroupId() throws Exception {
        given(userService.findByGroupId(1L)).willReturn(new ArrayList<>());
        mvc.perform(
                get("/api/v1/groups/1/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void clearCache() throws Exception {
        mvc.perform(
                delete("/api/v1/groups/cache"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void clearCacheById() throws Exception {
        mvc.perform(
                delete("/api/v1/groups/1/cache"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

}
