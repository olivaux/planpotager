package eu.planpotager.PlanPotager.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.planpotager.PlanPotager.user.dao.UserDAO;
import eu.planpotager.PlanPotager.user.domain.User;
import eu.planpotager.PlanPotager.user.dto.UserDTO;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthService authService;

    @Test
    void checkEmail_shouldReturnEmptyOptional_whenEmailIsNotRegistered() {
        String email = "jane.doe@example.com";
        when(userDAO.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = authService.checkEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    void checkEmail_shouldReturnUser_whenEmailIsAlreadyRegistered() {
        String email = "jane.doe@example.com";
        User existing = new User(email);
        when(userDAO.findByEmail(email)).thenReturn(Optional.of(existing));

        Optional<User> result = authService.checkEmail(email);

        assertThat(result).contains(existing);
    }

    @Test
    void createUser_shouldPersistNewUser_andReturnMatchingDTO() {
        String email = "jane.doe@example.com";
        when(userDAO.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = authService.createUser(email);

        assertThat(result.email()).isEqualTo(email);
        verify(userDAO).save(org.mockito.ArgumentMatchers.any(User.class));
    }
}
