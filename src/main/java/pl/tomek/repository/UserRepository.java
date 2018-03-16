package pl.tomek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.tomek.model.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLogin(String login);
    User findFirstByEmail(String email);

}
