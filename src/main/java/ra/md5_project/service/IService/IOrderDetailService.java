package ra.md5_project.service.IService;

import org.springframework.data.repository.query.Param;
import ra.md5_project.model.OrderDetail;
import ra.md5_project.service.IGenericService;

import java.util.List;

public interface IOrderDetailService extends IGenericService<OrderDetail> {

    List<OrderDetail> findOrderDetailsByOrderId(Long id);
    void deleteOrderDetailByOrder_Id(Long id);
    List<OrderDetail> findOrderDetailByOrOrderIdAndStoreId(Long id);


}
