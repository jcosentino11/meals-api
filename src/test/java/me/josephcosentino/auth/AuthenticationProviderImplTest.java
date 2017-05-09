package me.josephcosentino.auth;

import me.josephcosentino.auth.impl.AuthenticationProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Joseph Cosentino.
 */
@RunWith(SpringRunner.class)
public class AuthenticationProviderImplTest {

    @Mock
    private AuthenticationProviderImpl authenticationProvider;

    @Test
    public void authenticationProvided() {
        when(authenticationProvider.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("user", "pwd"));
        assertNotNull(authenticationProvider.getAuthentication());
    }
}
