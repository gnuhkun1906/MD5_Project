package ra.md5_project.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import ra.md5_project.model.Product;
import ra.md5_project.model.Users;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseStore {
    private Long id;
    private String storeName;
    private String address;
    private String phone;
    private boolean status;
    @JsonIgnoreProperties({"fullName", "sex", "avatar", "phone",
            "address", "createdDate", "status", "listRoles", "order", "productReviewsList"})
    private Users user;
    private List<Product> listProduct;
}
