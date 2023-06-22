package ra.md5_project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfile {
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Boolean sex;
    private String avatar;
    private String address;

}
