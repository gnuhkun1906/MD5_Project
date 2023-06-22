package ra.md5_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Product;

import java.util.List;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product,Long> {
    Page<Product> findByProductNameContaining(String name,Pageable pageable);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findProductByStatus(boolean status,Pageable pageable);

}



