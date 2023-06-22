package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(0)
    private float totalPrice;
    @NotBlank
    private String receiverName;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "date")
    private String createdDate;
    private String timeReceiver;
    @Column(columnDefinition = "tinyint")
    private int status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"order","fullName","email","sex","avatar",
    "phone","address","createdDate","status","listRoles","productReviewsList","store"
    })
//    @JsonIgnore
    private Users user;

    @OneToMany(mappedBy = "order", targetEntity = OrderDetail.class,fetch = FetchType.EAGER)
//    @JsonIgnoreProperties({"order","listOrderDetail"})
    @JsonIgnore
   private List<OrderDetail> listOrderDetail;


}
