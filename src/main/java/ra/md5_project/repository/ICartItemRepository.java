package ra.md5_project.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ra.md5_project.model.CartItems;
import ra.md5_project.model.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends PagingAndSortingRepository<CartItems, Long> {
        Optional<CartItems> findCartItemsByProductId(Long id);
        List<CartItems> findCartItemsByUserId(Long id);

        @Query("select sum(c.price) from CartItems as c where c.user.id=:userId")
        float sumPriceByUserId(@Param("userId") Long userId);
}
