package com.buoi1.webbanhang.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, unique = true)
    @NotBlank(message = "Tên người dùng là bắt buộc")
    @Size(min = 1, max = 50, message = "Tên người dùng phải từ 1 đến 50 ký tự")
    private String username;

    @Column(name = "password", length = 250)
    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;

    @Column(name = "confirm_password")
    @Transient // Đánh dấu trường này không cần map với cột trong cơ sở dữ liệu
    private String confirmPassword;


    @Column(name = "email", length = 50, unique = true)
    @NotBlank(message = "Email là bắt buộc")
    @Size(min = 1, max = 50, message = "Email phải từ 1 đến 50 ký tự")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "phone", length = 10, unique = true)
    @Length(min = 10, max = 10, message = "Số điện thoại phải là 10 chữ số")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại chỉ được chứa số")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "provider", length = 50)
    private String provider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
