package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.ProductReviews;
import ra.md5_project.repository.IProductReviewRepository;
import ra.md5_project.service.IService.IProductReviewService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductReviewService implements IProductReviewService {
    private final IProductReviewRepository productReviewRepository;

    @Override
    public Page<ProductReviews> findAll(Pageable pageable) {
        return productReviewRepository.findAll(pageable);
    }

    @Override
    public Optional<ProductReviews> findById(Long id) {
        return productReviewRepository.findById(id);
    }

    @Override
    public ProductReviews save(ProductReviews productReviews) {
        return productReviewRepository.save(productReviews);
    }

    @Override
    public void delete(Long id) {
        productReviewRepository.deleteById(id);
    }

    @Override
    public List<ProductReviews> findProductReviewsByProductId(Long id) {
        return productReviewRepository.findProductReviewsByProductId(id);
    }
}
