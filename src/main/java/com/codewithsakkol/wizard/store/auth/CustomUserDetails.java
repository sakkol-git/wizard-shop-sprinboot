package com.codewithsakkol.wizard.store.auth;

import com.codewithsakkol.wizard.store.users.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Data
public class CustomUserDetails implements UserDetails, OAuth2User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;


    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRole() != null 
                ? List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                : Collections.emptyList();
    }
    
    // Additional constructor for JWT token parsing
    public CustomUserDetails(Long id, String email, String roleStr) {
        this.id = id;
        this.name = null;
        this.email = email;
        this.password = null;
        this.authorities = roleStr != null 
                ? List.of(new SimpleGrantedAuthority("ROLE_" + roleStr))
                : Collections.emptyList();
    }



    @Override
    public @Nullable <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; // Can be null for stateless JWT requests
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
