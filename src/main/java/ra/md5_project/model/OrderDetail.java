package ra.md5_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(0)
    private float price;
    @Min(0)
    private int quantity;
//    private boolean status;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"list","category","store","productReviewsList"
          ,"tittle","description","quantity","price","createdDate","status","stock" , "cartItemsList"})
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
//    @JsonIgnoreProperties({"listOrderDetail","user"})
    @JsonIgnore
    private Order order;

}
