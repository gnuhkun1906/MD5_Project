package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.CartItems;
import ra.md5_project.repository.ICartItemRepository;
import ra.md5_project.service.IService.ICartItemService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final ICartItemRepository cartItemRepository;

    @Override
    public Iterable<CartItems> findCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public Optional<CartItems> findCartItemsByProductId(Long id) {
        return cartItemRepository.findCartItemsByProductId(id);
    }

    @Override
    public List<CartItems> findCartItemsByUserId(Long id) {
        return cartItemRepository.findCartItemsByUserId(id);
    }

    @Override
    public float sumPriceByUserId(Long userId) {
        return cartItemRepository.sumPriceByUserId(userId);
    }

    @Override
    public Page<CartItems> findAll(Pageable pageable) {

        return cartItemRepository.findAll(pageable);
    }

    @Override
    public Optional<CartItems> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public CartItems save(CartItems cartItems) {
        return cartItemRepository.save(cartItems);
    }

    @Override
    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

//    @Override
//    public Page<CartItems> findCartItemsByUserId(Long id) {
//        return cartItemRepository.findCartItemsByUserId(id);
//    }
}
