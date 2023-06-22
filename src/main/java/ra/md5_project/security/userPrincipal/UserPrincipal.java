package ra.md5_project.security.userPrincipal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ra.md5_project.model.Users;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {

    private Long id;
    private String fullName;
    private String userName;
    private String email;
    @JsonIgnore
    private String password;
    private String avatar;
    private String phone;
    private String createdDate;
    private boolean status;
    private Collection<? extends GrantedAuthority> roles;



    public static UserPrincipal build(Users user) {

        List<GrantedAuthority> grantedAuthorities =  user.getListRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());

        return UserPrincipal.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .userName(user.getUserName())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .phone(user.getPhone())
                .createdDate(user.getCreatedDate())
                .password(user.getPassword())
                .status(user.isStatus())
                .roles(grantedAuthorities)
                .build();


    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
    // Tài khoản của bạn có không bị hết hay không?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
   // Tài khoản của bạn có không bị block hay không?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // Mã của bạn có không bị hết hạn hay không?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //Tài khoản đã đc kích hoạt hay chưa?
    @Override
    public boolean isEnabled() {
        return true;
    }



}
