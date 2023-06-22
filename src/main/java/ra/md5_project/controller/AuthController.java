package ra.md5_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.md5_project.dto.request.SignInForm;
import ra.md5_project.dto.request.SignUpForm;
import ra.md5_project.dto.response.JwtResponse;
import ra.md5_project.dto.response.ResponseMessage;
import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;
import ra.md5_project.model.Users;
import ra.md5_project.security.jwt.JwtProvider;
import ra.md5_project.security.userPrincipal.UserPrincipal;
import ra.md5_project.service.serviceIPM.EmailSenderService;
import ra.md5_project.service.serviceIPM.RoleService;
import ra.md5_project.service.serviceIPM.UserService;


import javax.mail.MessagingException;
import javax.validation.Valid;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v3/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final EmailSenderService emailSenderService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) throws MessagingException {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors= bindingResult.getFieldErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError f:fieldErrors) {
                errorMessage.append(f.getField())
                        .append(" : ")
                        .append(f.getDefaultMessage())
                        .append(" ; ");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseMessage("Failed","Validation",errorMessage.toString())
            );
        }


        if (userService.existsByUserName(signUpForm.getUserName())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    ResponseMessage.builder()
                            .status("Failed")
                            .message("UserName is already existed ")
                            .data("")
                            .build()
            );
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                    ResponseMessage.builder()
                            .status("Failed")
                            .message("Email is already existed ")
                            .data("")
                            .build()
            );
        }

        Set<String> strRoles = signUpForm.getRole();
        Set<Roles> roles = new HashSet<>();
        if (strRoles == null || strRoles.isEmpty()) {
            Roles role = roleService.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Not Found Role"));
            roles.add(role);
        } else {
            strRoles.forEach(
                    role -> {
                        switch (role) {
                            case "admin":
                                Roles admRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Not Found Role"));
                                roles.add(admRole);
                            case "sm":
                                Roles pmRole = roleService.findByRoleName(ERole.ROLE_SM)
                                        .orElseThrow(() -> new RuntimeException("Not Found Role"));
                                roles.add(pmRole);
                            case "seller":
                                Roles sellerRole = roleService.findByRoleName(ERole.ROLE_SELLER)
                                        .orElseThrow(() -> new RuntimeException("Not Found Role"));
                                roles.add(sellerRole);
                            case "user":
                                Roles userRole = roleService.findByRoleName(ERole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Not Found Role"));
                                roles.add(userRole);
                        }
                    }
            );
        }

//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date dateNow = new Date();
//            String strNow = sdf.format(dateNow);
            Users user = Users.builder()
                    .fullName(signUpForm.getFullName())
                    .userName(signUpForm.getUserName())
                    .email(signUpForm.getEmail())
                    .phone(signUpForm.getPhone())
                    .password(passwordEncoder.encode(signUpForm.getPassword()))
                    .createdDate(LocalDate.now().toString())
                    .status(true)
                    .sex(signUpForm.isSex())
                    .listRoles(roles)
                    .build();
        Users users = userService.save(user);

        String html = "<b> Hello guys" +"<br>" +"<i>Welcome to my home </i><br>"
                +"<b> Username : </b>"+user.getUserName()+"<br>"
                +"<b> Password : </b>"+signUpForm.getPassword();
        emailSenderService.sendEmail(user.getEmail(),
                "Register successfully!!",html
        );
            return ResponseEntity.ok().body(
                    ResponseMessage.builder()
                            .status("OK")
                            .message("Created successfully")
                            .data(users)
                            .build()
            );
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@RequestBody SignInForm signInForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInForm.getUserName(), signInForm.getPassword())
            );
            String token = jwtProvider.createToken(authentication);
            UserPrincipal userPrincipal= (UserPrincipal) authentication.getPrincipal();
            return new ResponseEntity<>(
                    JwtResponse.builder()
                            .status("OK")
                            .type("Bearer")
                            .fullName(userPrincipal.getFullName())
                            .token(token)
                            .roles(userPrincipal.getAuthorities())
                            .build(),HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(
                    ResponseMessage.builder()
                            .status("Failed")
                            .message("UserName or Password incorrect")
                            .data("")
                            .build(), HttpStatus.UNAUTHORIZED);
        }
    }

}
