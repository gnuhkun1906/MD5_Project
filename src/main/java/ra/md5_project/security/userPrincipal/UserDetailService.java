package ra.md5_project.security.userPrincipal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ra.md5_project.model.Users;
import ra.md5_project.service.IService.IUserService;


@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userService.findByUserName(username);
        if (users == null) {
            throw  new UsernameNotFoundException("Not Found User -> Message{}"+ username);
        }
        return UserPrincipal.build(users);

    }

    public Users getUserFromAuthentication() {
        UserPrincipal userPrincipal= (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findUsersById(userPrincipal.getId()).get();
    }
}
