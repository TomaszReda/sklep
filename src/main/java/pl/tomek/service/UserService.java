package pl.tomek.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.UserRepository;
import pl.tomek.repository.UserRoleRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    public static final String DEFAULT_ROLE="DEFAULT_USER";

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void addwithDefaultRole(User user)
    {

        UserRole userRole=userRoleRepository.findByRole(DEFAULT_ROLE);
        System.err.println(userRole);
        user.getUserRoleSet().add(userRole);
        userRepository.save(user);
    }


}
