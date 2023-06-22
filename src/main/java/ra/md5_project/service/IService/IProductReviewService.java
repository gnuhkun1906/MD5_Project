package ra.md5_project.service.IService;

import ra.md5_project.model.ProductReviews;
import ra.md5_project.service.IGenericService;

import java.util.List;

public interface IProductReviewService extends IGenericService<ProductReviews> {
    List<ProductReviews> findProductReviewsByProductId(Long id);

}
