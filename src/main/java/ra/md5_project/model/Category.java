package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String categoryName;
//    @JsonIgnore
    private boolean status=true;
    @JsonIgnore
    @OneToMany(mappedBy = "category",targetEntity = Product.class,fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"category"})
    private List<Product> listProduct;

}
