package eu.planpotager.PlanPotager.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.planpotager.PlanPotager.garden.dao.GardenDAO;
import eu.planpotager.PlanPotager.notification.dao.NotificationDAO;
import eu.planpotager.PlanPotager.notification.domain.Notification;
import eu.planpotager.PlanPotager.notification.dto.NotifDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotifServiceTest {

    private static final String USER_EMAIL = "jane.doe@example.com";

    @Mock
    private NotificationDAO notificationDAO;

    @Mock
    private GardenDAO gardenDAO;

    @InjectMocks
    private NotifService notifService;

    @Test
    void getNotificationsByUser_shouldReturnDTOsOrderedByDate() {
        Notification older = new Notification("Arrosage recommande", "CONSEIL", LocalDateTime.now().minusDays(1), USER_EMAIL);
        Notification newer = new Notification("Recolte disponible", "RECOLTE", LocalDateTime.now(), USER_EMAIL);
        when(notificationDAO.findByUserEmailOrderByCreatedAtDesc(USER_EMAIL)).thenReturn(List.of(newer, older));

        List<NotifDTO> result = notifService.getNotificationsByUser(USER_EMAIL);

        assertThat(result).extracting(NotifDTO::message)
                .containsExactly("Recolte disponible", "Arrosage recommande");
    }

    @Test
    void getNotificationsByUser_shouldReturnEmptyList_whenUserHasNoNotification() {
        when(notificationDAO.findByUserEmailOrderByCreatedAtDesc(USER_EMAIL)).thenReturn(List.of());

        List<NotifDTO> result = notifService.getNotificationsByUser(USER_EMAIL);

        assertThat(result).isEmpty();
    }

    @Test
    void setNotifAsRead_shouldMarkNotificationAsRead_andPersist() {
        Notification notif = new Notification("Arrosage recommande", "CONSEIL", LocalDateTime.now(), USER_EMAIL);
        when(notificationDAO.findById(7L)).thenReturn(Optional.of(notif));
        when(notificationDAO.save(notif)).thenReturn(notif);

        notifService.setNotifAsRead(7L);

        assertThat(notif.getRead()).isTrue();
        verify(notificationDAO, times(1)).save(notif);
    }

    @Test
    void addNotifications_shouldPersistOneNotificationPerDTO() {
        NotifDTO first = new NotifDTO(null, "Arrosage recommande", "CONSEIL", false, LocalDateTime.now());
        NotifDTO second = new NotifDTO(null, "Recolte disponible", "RECOLTE", false, LocalDateTime.now());
        when(notificationDAO.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        notifService.addNotifications(List.of(first, second), USER_EMAIL);

        verify(notificationDAO, times(2)).save(any(Notification.class));
    }
}
