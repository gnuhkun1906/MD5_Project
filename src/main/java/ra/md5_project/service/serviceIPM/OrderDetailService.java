package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.OrderDetail;
import ra.md5_project.repository.IOrderDetailRepository;
import ra.md5_project.service.IService.IOrderDetailService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final IOrderDetailRepository orderDetailRepository;

    @Override
    public Page<OrderDetail> findAll(Pageable pageable) {
        return orderDetailRepository.findAll(pageable);
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findOrderDetailsByOrderId(Long id) {
        return orderDetailRepository.findOrderDetailsByOrderId(id);
    }

    @Override
    public void deleteOrderDetailByOrder_Id(Long id) {
        orderDetailRepository.deleteOrderDetailByOrder_Id(id);
    }

    @Override
    public List<OrderDetail> findOrderDetailByOrOrderIdAndStoreId(Long id) {
        return orderDetailRepository.findOrderDetailByOrOrderIdAndStoreId(id);
    }
}
