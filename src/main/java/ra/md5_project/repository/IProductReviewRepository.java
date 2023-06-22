package ra.md5_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Product;
import ra.md5_project.model.ProductReviews;

import java.util.List;

@Repository
public interface IProductReviewRepository extends JpaRepository<ProductReviews, Long> {
    List<ProductReviews> findProductReviewsByProductId(Long id);

}
