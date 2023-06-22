package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "product_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    ProductReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String contents;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "date")
    private String createdDate;
    @OneToMany(mappedBy = "productReviews", targetEntity = Image.class, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"productReviews"})
    private List<Image> image;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"productReviewsList","fullName","email","sex","avatar",
    "phone","address","createdDate","status","listRoles","order","store"
    })
    private Users user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"productReviewsList","tittle","description","quantity","price",
   "createdDate","status","stock","category","store","listImage","cartItemsList"
    })
    private Product product;
}
