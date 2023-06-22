package ra.md5_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Category;

@Repository
public interface ICategoryRepository extends PagingAndSortingRepository<Category, Long> {
        Page<Category> findAll(Pageable pageable);
}
