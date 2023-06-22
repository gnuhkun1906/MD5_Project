package ra.md5_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.Store;

import java.util.Optional;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findStoreByUserId(Long id);
}
