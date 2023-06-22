package ra.md5_project.service.IService;


import ra.md5_project.model.CartItems;
import ra.md5_project.service.IGenericService;

import java.util.List;
import java.util.Optional;

public interface ICartItemService extends IGenericService<CartItems> {

    Iterable<CartItems> findCartItems();
    Optional<CartItems> findCartItemsByProductId(Long id);
    List<CartItems> findCartItemsByUserId(Long id);
    float sumPriceByUserId(Long userId);



}
