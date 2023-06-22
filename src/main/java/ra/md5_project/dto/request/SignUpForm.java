package ra.md5_project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {
    @NotBlank(message = "Please don't leave blank")
    @Size(min = 6, message = "FullName must be more than 6 character")
    private String fullName;
    @NotBlank(message = "Please don't leave blank")
    @Size(min = 6,message = "FullName must be more than 6 character")
    private String userName;
    private String password;
    @Email
    @NotBlank(message = "Please don't leave blank")
    private String email;
    private boolean sex;
    private String phone;
    private Set<String> role;

}
