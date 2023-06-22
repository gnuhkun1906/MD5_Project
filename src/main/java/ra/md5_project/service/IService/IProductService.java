package ra.md5_project.service.IService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.md5_project.model.Product;
import ra.md5_project.service.IGenericService;

import java.util.List;

public interface IProductService extends IGenericService<Product> {

    Page<Product> findByProductNameContaining(String name, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    Iterable<Product> findProducts();
    Page<Product> findProductByStatus(boolean status,Pageable pageable);

}
