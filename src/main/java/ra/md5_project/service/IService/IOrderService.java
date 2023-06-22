package ra.md5_project.service.IService;

import org.springframework.data.domain.Pageable;
import ra.md5_project.model.Order;
import ra.md5_project.service.IGenericService;

import java.util.List;

public interface IOrderService extends IGenericService<Order> {
    List<Order> findOrderByUserId(Long id, Pageable pageable);
    List<Order> findOrderByStoreId(Long id);

}
