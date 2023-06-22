package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Please don't leave blank")
    @Size(min = 6, message = "FullName must be more than 6 character")
    private String fullName;
    @NotBlank(message = "Please don't leave blank")
    @Size(min = 6,message = "FullName must be more than 6 character")
    private String userName;
    @Email
    @NotBlank(message = "Please don't leave blank")
    private String email;
    private boolean sex;
    @Lob
    private String avatar;
    @NotBlank(message = "Please don't leave blank")
    @Size(min = 6,message = "FullName must be more than 6 character")
    @JsonIgnore
    private String password;
    @Size(min = 10, max = 11,message = "Your Phone number 10 or 11")
    private String phone;
    private String address;
    @Column(columnDefinition = "date")
    private String createdDate;
    private boolean status= true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> listRoles;

    @OneToMany(mappedBy = "user",targetEntity = Order.class, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Order> order;

    @OneToMany(mappedBy = "user", targetEntity = ProductReviews.class,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<ProductReviews> productReviewsList;

    @OneToOne
    @JsonIgnoreProperties({"user"})
    private Store store;

}
