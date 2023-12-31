package com.tiamat.auth.services;

import com.tiamat.auth.entities.UserEntity;
import com.tiamat.auth.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Error: User not found."));

        String[] roles ={ userEntity.getRole() };

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(this.grantedAuthorityList(roles))
                .accountLocked(userEntity.isLocked())
                .disabled(userEntity.isDisabled())
                .build();
    }

    private List<GrantedAuthority> grantedAuthorityList(String[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for(String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

}
