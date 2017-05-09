package me.josephcosentino.service;


import me.josephcosentino.InvalidateCache;
import me.josephcosentino.exception.NotFoundException;
import me.josephcosentino.exception.UnauthorizedException;
import me.josephcosentino.model.Role;
import me.josephcosentino.model.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("me.josephcosentino")
@WithMockUser(username = "admin", roles = "ADMIN")
public class UserServiceImplTest {

    @Configuration
    @EnableCaching
    static class CacheConfig {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("user");
        }

    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    @Autowired
    public InvalidateCache invalidateCache;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @Sql("/load-user.sql")
    public void findUserById() {
        User user = userService.findById(1L);
        assertEquals(user.getUsername(), "test-user");
    }

    @Test
    public void findNonExistentUserById() {
        exception.expect(NotFoundException.class);
        userService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    public void findByIdCache() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername("test-user");

        User user = userService.findById(1L);
        assertNotNull(user);
        assertEquals(user, expectedUser);

        User cachedUser = (User) cacheManager.getCache("user").get(1L).get();
        assertNotNull(cachedUser);
        assertEquals(cachedUser, expectedUser);
    }

    @Test
    @Transactional
    @Sql("/load-user-group.sql")
    public void findUserByGroupId() {
        List<User> users = userService.findByGroupId(1L);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getUsername(), "test-user");
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    public void findAllUsersByGroupId() {
        List<User> users = userService.findByGroupId(1L);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0).getUsername(), "test-user");
        assertEquals(users.get(1).getUsername(), "test-user-2");
    }

    @Test
    public void findUsersByNonExistentGroupId() {
        exception.expect(NotFoundException.class);
        userService.findByGroupId(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    @SuppressWarnings("unchecked")
    public void findByGroupIdCache() {
        User expectedUser1 = new User();
        expectedUser1.setId(1L);
        expectedUser1.setUsername("test-user");

        User expectedUser2 = new User();
        expectedUser2.setId(2L);
        expectedUser2.setUsername("test-user-2");

        List<User> users = userService.findByGroupId(1L);
        assertNotNull(users);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), expectedUser1);
        assertEquals(users.get(1), expectedUser2);

        List<User> cachedUsers = (List<User>) cacheManager.getCache("user").get(1L).get();
        assertNotNull(cachedUsers);
        assertEquals(cachedUsers.size(), 2);
        assertEquals(cachedUsers.get(0), expectedUser1);
        assertEquals(cachedUsers.get(1), expectedUser2);
    }

    @Test
    public void findNonExistantUsers() {
        List<User> users = userService.findAll(null);
        assertTrue(users.isEmpty());
    }

    @Test
    @Transactional
    @Sql("/load-user.sql")
    public void findUser() {
        List<User> users = userService.findAll(null);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getUsername(), "test-user");
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    public void findUsers() {
        List<User> users = userService.findAll(null);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0).getUsername(), "test-user");
        assertEquals(users.get(1).getUsername(), "test-user-2");
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    @SuppressWarnings("unchecked")
    public void findAllCache() {
        User expectedUser1 = new User();
        expectedUser1.setId(1L);
        expectedUser1.setUsername("test-user");
        expectedUser1.setPassword("pwd");

        User expectedUser2 = new User();
        expectedUser2.setId(2L);
        expectedUser2.setUsername("test-user-2");
        expectedUser2.setPassword("pwd");

        Pageable page = new PageRequest(0, Integer.MAX_VALUE);

        List<User> users = userService.findAll(page);
        assertNotNull(users);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0), expectedUser1);
        assertEquals(users.get(1), expectedUser2);

        List<User> cachedUsers = (List<User>) cacheManager.getCache("user").get(page).get();
        assertNotNull(cachedUsers);
        assertEquals(cachedUsers.size(), 2);
        assertEquals(cachedUsers.get(0), expectedUser1);
        assertEquals(cachedUsers.get(1), expectedUser2);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessFindAll() {
        userService.findAll(new PageRequest(0, Integer.MAX_VALUE));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void userAccessFindAll() {
        exception.expect(UnauthorizedException.class);
        userService.findAll(new PageRequest(0, Integer.MAX_VALUE));
    }

    @Test
    @Transactional
    @Sql("/load-user.sql")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessFindById() {
        userService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    public void validUserAccessFindById() {
        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(1L);

        setSpringSecurityUser(user);

        userService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    public void invalidUserAccessFindById() {
        exception.expect(UnauthorizedException.class);

        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user-2");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(2L);

        setSpringSecurityUser(user);

        userService.findById(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessFindByGroup() {
        userService.findByGroupId(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    public void validUserAccessFindByGroup() {
        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(1L);

        setSpringSecurityUser(user);

        userService.findByGroupId(1L);
    }

    @Test
    @Transactional
    @Sql("/load-users-group.sql")
    public void invalidUserAccessFindByGroup() {
        exception.expect(UnauthorizedException.class);

        Role role = new Role();
        role.setValue("ROLE_USER");

        User user = new User();
        user.setUsername("test-user-3");
        user.setPassword("pwd");
        user.getRoles().add(role);
        user.setId(3L);

        setSpringSecurityUser(user);

        userService.findByGroupId(1L);
    }

    private void setSpringSecurityUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    @SuppressWarnings("unchecked")
    public void clearCache() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test-user");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test-user-2");
        user2.setPassword("pwd");

        userService.findById(1L);
        userService.findById(2L);

        User cachedUser1 = cacheManager.getCache("user").get(1L, User.class);
        User cachedUser2 = cacheManager.getCache("user").get(2L, User.class);

        assertNotNull(cachedUser1);
        assertNotNull(cachedUser2);

        userService.clearCache();

        assertNull(cacheManager.getCache("user").get(1L));
        assertNull(cacheManager.getCache("user").get(2L));
    }

    @Test
    @Transactional
    @Sql("/load-users.sql")
    @SuppressWarnings("unchecked")
    public void clearCacheById() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("test-user");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("test-user-2");
        user2.setPassword("pwd");

        userService.findById(1L);
        userService.findById(2L);

        User cachedUser1 = cacheManager.getCache("user").get(1L, User.class);
        User cachedUser2 = cacheManager.getCache("user").get(2L, User.class);

        assertNotNull(cachedUser1);
        assertNotNull(cachedUser2);

        userService.clearCacheById(1L);

        assertNull(cacheManager.getCache("user").get(1L));
        assertNotNull(cacheManager.getCache("user").get(2L));
    }

}
