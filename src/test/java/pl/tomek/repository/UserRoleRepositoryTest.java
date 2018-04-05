package pl.tomek.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.tomek.model.UserRole;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    private UserRole userRole;

    @Before
    public void setUp()  {
        userRole=new UserRole(2L,"ROLE","TEST ROLE");
        userRoleRepository.save(userRole);
    }


    @Test
    public void findByRole1() {
        Assert.assertNotNull(userRoleRepository.findByRole(userRole.getRole()));
    }

    @Test
    public void findAll() {
        Assert.assertNotNull(userRoleRepository.findAll());
    }
}