package ra.md5_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Order;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrderByUserId(Long id, Pageable pageable);

    @Query(value = "select o.* from orders o join order_detail od on o.id = od.order_id join products p on p.id = od.product_id join stores s on s.id = p.store_id where s.id= :id", nativeQuery = true)
    List<Order> findOrderByStoreId(@Param("id") Long id);
}
