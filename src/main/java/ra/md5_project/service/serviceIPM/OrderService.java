package ra.md5_project.service.serviceIPM;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.Order;
import ra.md5_project.repository.IOrderRepository;
import ra.md5_project.service.IService.IOrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findOrderByUserId(Long id,Pageable pageable) {
        return orderRepository.findOrderByUserId(id,pageable);
    }

    @Override
    public List<Order> findOrderByStoreId(Long id) {
        return orderRepository.findOrderByStoreId(id);
    }
}
