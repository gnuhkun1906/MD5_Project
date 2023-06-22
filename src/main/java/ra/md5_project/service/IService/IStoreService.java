package ra.md5_project.service.IService;

import org.springframework.data.domain.Pageable;
import ra.md5_project.model.Store;
import ra.md5_project.service.IGenericService;

import java.util.Optional;

public interface IStoreService extends IGenericService<Store> {
    Optional<Store> findStoreByUserId(Long id);


}
