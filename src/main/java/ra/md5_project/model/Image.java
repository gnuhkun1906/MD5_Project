package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String fileType;
    @Lob
    private  byte[] data;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"listImage"})
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_review_id")
    @JsonIgnoreProperties({"image"})
    private ProductReviews productReviews;


}
