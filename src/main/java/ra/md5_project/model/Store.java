package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "stores")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String storeName;
    @NotBlank
    private String  address;
    @NotBlank
    private String phone;
    private boolean status;
    @OneToOne
    @JoinColumn(name = "user_id",unique = true)
    @JsonIgnoreProperties({"fullName","sex","avatar","phone","address",
            "createdDate","status","listRoles","order","productReviewsList",
            "listProduct","store"})
    private Users user;
    @OneToMany(mappedBy = "store",targetEntity = Product.class, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Product> listProduct;

}
