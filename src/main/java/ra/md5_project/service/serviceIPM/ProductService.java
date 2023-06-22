package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.Product;
import ra.md5_project.repository.ICategoryRepository;
import ra.md5_project.repository.IProductRepository;
import ra.md5_project.service.IService.ICategoryService;
import ra.md5_project.service.IService.IProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final IProductRepository productRepository;


    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findByProductNameContaining(String name, Pageable pageable) {
        return productRepository.findByProductNameContaining(name, pageable);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
//        pageable= PageRequest.of(pageable.getPageNumber(), 10);
        return productRepository.findAll(pageable);
    }

    @Override
    public Iterable<Product> findProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findProductByStatus(boolean status,Pageable pageable) {
        return productRepository.findProductByStatus(status, pageable);
    }


}
