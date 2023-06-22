package ra.md5_project.service.serviceIPM;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.md5_project.model.Store;
import ra.md5_project.repository.IStoreRepository;
import ra.md5_project.service.IService.IStoreService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService implements IStoreService {

    private final IStoreRepository storeRepository;
    @Override
    public Page<Store> findAll(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }

    @Override
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    @Override
    public Optional<Store> findStoreByUserId(Long id) {
        return storeRepository.findStoreByUserId(id);
    }
}
