package eu.planpotager.PlanPotager.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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
class ProfileServiceTest {

    private static final String EMAIL = "jane.doe@example.com";

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void getProfile_shouldReturnDTO_matchingStoredUser() {
        User stored = new User(EMAIL);
        stored.setUnit("METRIC");
        stored.setLanguage("fr");
        when(userDAO.findByEmail(EMAIL)).thenReturn(Optional.of(stored));

        UserDTO result = profileService.getProfile(EMAIL);

        assertThat(result.email()).isEqualTo(EMAIL);
        assertThat(result.unit()).isEqualTo("METRIC");
        assertThat(result.language()).isEqualTo("fr");
    }

    @Test
    void updateProfile_shouldSaveUpdatedUnitAndLanguage() {
        User stored = new User(EMAIL);
        when(userDAO.findByEmail(EMAIL)).thenReturn(Optional.of(stored));
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = profileService.updateProfile(EMAIL, "IMPERIAL", "en");

        assertThat(result.unit()).isEqualTo("IMPERIAL");
        assertThat(result.language()).isEqualTo("en");
        verify(userDAO).save(stored);
    }

    @Test
    void deleteAccount_shouldDeleteStoredUser() {
        User stored = new User(EMAIL);
        when(userDAO.findByEmail(EMAIL)).thenReturn(Optional.of(stored));

        profileService.deleteAccount(EMAIL);

        verify(userDAO, times(1)).delete(stored);
    }

}
