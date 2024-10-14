package io.lynx.oebs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "accounts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID accountId;
    private String username;
    @JsonIgnore
    private String password;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String phoneNumber;
    private String lang;
    @Column(nullable = false)
    private boolean isEmailVerified = false;
    @Column(nullable = false)
    private boolean isPhoneVerified = false;
    private String name;
    private String otp;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities/roles for this user
        return List.of(); // or List.of(new SimpleGrantedAuthority("ROLE_USER")) if roles are needed
    }

    @Override
    public String getUsername() {
        return email; // Set email as the username for authentication purposes
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize as needed
    }

    @Override
    public boolean isEnabled() {
        return isEmailVerified || isPhoneVerified; // Enabled only if email is verified
    }
}


