/**
 * @author falvesmac
 */

package com.gestpoint.gestpoint_api.infra.security;

import com.gestpoint.gestpoint_api.domain.User;
import com.gestpoint.gestpoint_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), authorities);
    }
}