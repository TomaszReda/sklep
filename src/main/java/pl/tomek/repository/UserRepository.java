package pl.tomek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tomek.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);
}
