package pl.tomek.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;

import java.util.HashSet;
import java.util.Set;




@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @Before
        public void setUp() throws Exception {
        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        user=new User(2L,"imie","nazwisko","tomekreda@op.pl","login","passworld",userRoleSet,true,true);
        userRepository.save(user);
    }

    @Test
    public void findByLogin() {

        Assert.assertNotNull(userRepository.findByLogin(user.getLogin()));
    }

    @Test
    public void findFirstByEmail() {
        Assert.assertNotNull(userRepository.findFirstByEmail(user.getEmail()));
    }

    @Test
    public void findAll() {
        Assert.assertNotNull(userRepository.findAll());
    }

}