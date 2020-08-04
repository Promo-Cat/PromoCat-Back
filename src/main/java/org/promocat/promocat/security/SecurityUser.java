package org.promocat.promocat.security;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class SecurityUser implements UserDetails {

    private final String telephone;
    private final AccountType accountType;

    public SecurityUser(final String telephone, final AccountType accountType) {
        this.telephone = telephone;
        this.accountType = accountType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + accountType.getType().toUpperCase()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return telephone;
    }

    @Override
    public String getUsername() {
        return telephone;
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
