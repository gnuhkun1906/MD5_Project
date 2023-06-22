package ra.md5_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.OrderDetail;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailsByOrderId(Long id);
    void deleteOrderDetailByOrder_Id(Long id);
    @Query(value = "SELECT od.* from order_detail od join products p on p.id = od.product_id join stores s on s.id = p.store_id where s.id= :id",nativeQuery = true)
    List<OrderDetail> findOrderDetailByOrOrderIdAndStoreId(@Param("id") Long id);

}
