package pl.tomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.tomek.model.User;
import pl.tomek.model.UserRole;
import pl.tomek.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

public class UserDetailImplement implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userRepository.findByLogin(s);
        org.springframework.security.core.userdetails.User users=new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassworld(),
                convert(user.getUserRoleSet())
        );
        return users;
    }
    private Set<GrantedAuthority> convert(Set<UserRole> userRoles)
    {
        Set<GrantedAuthority> set=new HashSet<>();
        for(UserRole u:userRoles)
        {
            set.add(new SimpleGrantedAuthority(u.getRole()));
        }
        return set;
    }
}
