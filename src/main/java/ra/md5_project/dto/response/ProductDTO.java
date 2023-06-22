package ra.md5_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.md5_project.model.Image;
import ra.md5_project.model.Store;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private  String productName;
    private String tittle;
    private String description;
    private float price;
    private boolean stock;
//    private Store store;
    private Image image;

}
