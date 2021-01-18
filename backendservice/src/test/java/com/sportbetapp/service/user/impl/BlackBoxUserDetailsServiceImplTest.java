package ua.training.services.impl.blackbox;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.sportbetapp.domain.user.role.Role;
import com.sportbetapp.repository.user.UserRepository;
import com.sportbetapp.service.user.impl.UserDetailsServiceImpl;


/**
 * Black Box Testing is a software testing method
 * in which the internal structure/ design/ implementation
 * of the item being tested is not known to the tester
 *
 * @author Stanislav_Holovachuk
 */
@RunWith(MockitoJUnitRunner.class)
public class BlackBoxUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl serviceUnderTest;

    @Before
    public void setUp() {
        when(userRepository.findByEmail(anyString())).thenReturn(prepareUser());
    }

    private com.sportbetapp.domain.user.User prepareUser() {
        return new com.sportbetapp.domain.user.User() {
            {
                setId(0L);
                setName("Name");
                setPassword("123");
                setEmail("name@gmail.com");
                setRoles(Set.of(prepareRole(0L, "ADMIN"), prepareRole(1L, "CLIENT")));
            }

            private Role prepareRole(long i, String name) {
                return new Role() {{
                    setId(i);
                    setName(name);
                }};
            }
        };
    }

    @Test
    public void loadUserByUsername() {
        org.springframework.security.core.userdetails.User actual =
                (User) serviceUnderTest.loadUserByUsername("anyStringDoesNotMatter");

        assertThat(actual, is(notNullValue()));
        assertThat(actual.getUsername(), is("name@gmail.com"));
        assertThat(actual.getPassword(), is("123"));

        Collection<GrantedAuthority> authorities = actual.getAuthorities();
        assertThat(authorities, hasSize(2));
        assertThat(authorities.stream().map(GrantedAuthority::getAuthority).collect(toList()),
                containsInAnyOrder("ADMIN", "CLIENT"));
//        assertThat(authorities, everyItem(hasProperty("role", is(notNullValue()))));

        Consumer<Integer> assertColumn = (column) -> assertThat(column, Matchers.is(greaterThanOrEqualTo(0)));
        actual.getAuthorities().forEach(authority -> assertColumn.accept(authority.getAuthority().length()));

    }

}
