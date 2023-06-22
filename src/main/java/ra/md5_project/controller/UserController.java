package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.omg.CORBA.UserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.request.ChangePassword;
import ra.md5_project.dto.request.ChangeRole;
import ra.md5_project.dto.request.UpdateProfile;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;
import ra.md5_project.model.Users;
import ra.md5_project.security.userPrincipal.UserDetailService;
import ra.md5_project.security.userPrincipal.UserPrincipal;
import ra.md5_project.service.IService.IRoleService;
import ra.md5_project.service.IService.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/findAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), 2);
        Page<Users> listUser = userService.findAll(pageable);
        return new ResponseEntity<>(listUser, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> detailByUserName(@RequestParam("userName") String userName) {
        Users users = userService.findByUserName(userName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/changeRole")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> changeRole(@RequestBody ChangeRole changeRole) {
        Long userId = changeRole.getUserId();
//        Users users = userService.findUsersById(userId).get();
        Optional<Users> usersOptional=userService.findUsersById(userId);
        if (!usersOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!!");
        }
        Set<Roles> setRole = usersOptional.get().getListRoles();
        if (changeRole.getNeedToDelete() != null) {
            if (changeRole.getNeedToDelete().equals("ROLE_SM") || changeRole.getNeedToDelete().equals("ROLE_SELLER")) {
                if (setRole.contains(roleService.findRolesByRoleName(ERole.valueOf("ROLE_ADMIN")).get())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseMessage("Failed", "You cannot delete with Role Admin!! ", "")
                    );
                } else {
                    setRole.remove(roleService.findRolesByRoleName(ERole.valueOf(changeRole.getNeedToDelete())).get());
                    userService.save(usersOptional.get());
                    return ResponseEntity.ok(new ResponseMessage("Ok", "DeleteRole successfully", ""));
                }
            }
        } else {
            if (changeRole.getNeedToUpdate().equals("ROLE_SM")) {
                if (setRole.contains(roleService.findRolesByRoleName(ERole.valueOf("ROLE_ADMIN")).get())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseMessage("Failed", "You cannot Update with Role Admin!! ", "")
                    );
                } else if (setRole.contains(roleService.findRolesByRoleName(ERole.valueOf("ROLE_SM")).get())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ResponseMessage("Failed", "User is ROLE_SM!! ", "")
                    );
                } else {
                    setRole.add(roleService.findRolesByRoleName(ERole.valueOf(changeRole.getNeedToUpdate())).get());
                    userService.save(usersOptional.get());
                    return ResponseEntity.ok(new ResponseMessage("Ok", "UpdateROLE successfully", ""));
                }
            }
        }
        return ResponseEntity.ok(
                new ResponseMessage("Ok", "You Change Role Success!", "")
        );
    }

    @PutMapping("/updateProfile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SELLER','ROLE_SM','ROLE_USER')")
    @Transactional(rollbackFor = UserException.class)
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfile user) {
        Long userId = userDetailService.getUserFromAuthentication().getId();
        Optional<Users> usersOptional = userService.findUsersById(userId);
        Users usersOld = usersOptional.get();
        Users users = Users.builder()
                .id(userId)
                .userName(user.getUserName() == null ? usersOld.getUserName() : user.getUserName())
                .fullName(user.getFullName() == null ? usersOld.getFullName() : user.getFullName())
                .email(user.getEmail() == null ? usersOld.getEmail() : user.getEmail())
                .sex(user.getSex()==null ? usersOld.isSex() : user.getSex())
                .avatar(user.getAvatar() == null ? usersOld.getAvatar() : user.getAvatar())
                .phone(user.getPhone() == null ? usersOld.getPhone() : user.getPhone())
                .address(user.getAddress() == null ? usersOld.getAddress() : user.getAddress())
                .password(usersOld.getPassword())
                .status(usersOld.isStatus())
                .createdDate(usersOld.getCreatedDate())
                .listRoles(usersOld.getListRoles())
                .productReviewsList(usersOld.getProductReviewsList())
                .order(usersOld.getOrder())
                .store(usersOld.getStore())
                .build();
        userService.save(users);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_SELLER','ROLE_SM','ROLE_ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword) {
        Users user = userDetailService.getUserFromAuthentication();
        String pass = user.getPassword();
        if (!passwordEncoder.matches(changePassword.getOldPassword(), pass)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "OldPassword doesn't match", null)
            );
        }
        if (!changePassword.getNewPassword().matches("[a-zA-Z]{6,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "Your password must be more than 6 characters!!", null)
            );
        } else if (!changePassword.getRePassword().matches(changePassword.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed", "RePassword doesn't match ", null)
            );
        } else {
            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userService.save(user);
            return ResponseEntity.ok("Change Password successfully!!");
        }
    }

    @PutMapping("/blockUser/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> blockUser (@PathVariable("id") Long id) {
       Optional<Users> usersOptional=userService.findUsersById(id);
        if (!usersOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        usersOptional.get().setStatus(false);
        return new ResponseEntity<>(userService.save(usersOptional.get()),HttpStatus.OK);
    }
}
