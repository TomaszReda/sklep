package pl.tomek.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.UserRepository;
import pl.tomek.repository.UserRoleRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Before
    public void setUp() throws Exception {
        Set<UserRole> userRoleSet=new HashSet<>();
        UserRole userRole=new UserRole();
        userRole.setDescription("Domyslna rola przy rejestracji");
        userRole.setRole("DEFAULT_USER");
        userRoleSet.add(userRole);
        User user=new User(2L,"imie","nazwisko","tomekreda@op.pl","login","passworld",userRoleSet,true,true);
        userRepository.save(user);


    }

    @Test
    public void addwithDefaultRole() {

    }
}