package eu.planpotager.PlanPotager.user.dao;

import eu.planpotager.PlanPotager.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
