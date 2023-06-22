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
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String productName;
    private String tittle;
    private String description;
    @NotNull
    @Min(0)
    private int quantity;
    @NotNull
    @Min(0)
    private float price;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "date")
    private String createdDate;
    private boolean status;

    private boolean stock = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"listProduct"})
    private Category category;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnoreProperties({"listProduct","phone","status","user"})
    private Store store;

    @OneToMany(mappedBy = "product", targetEntity = Image.class, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"product","data","fileType"})
    private List<Image> listImage;

    @OneToMany(mappedBy = "product", targetEntity = ProductReviews.class, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({""})
    private List<ProductReviews> productReviewsList;
    @OneToMany(mappedBy = "product", targetEntity = OrderDetail.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> listOrder;
    @OneToMany(mappedBy = "product", targetEntity = CartItems.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItems> cartItemsList;

}
