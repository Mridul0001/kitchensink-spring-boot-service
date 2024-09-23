package com.kitchensink.ks.data.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document("users")
public class UserDocument implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(){{
            add(new SimpleGrantedAuthority("ROLE_"+role));
        }};
    }
}
