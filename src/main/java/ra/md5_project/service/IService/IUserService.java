package ra.md5_project.service.IService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.md5_project.model.Users;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users findByUserName(String userName);
    Users save(Users users);
    Page<Users> findAll(Pageable pageable);
    Optional<Users> findUsersById(Long id);
//    List<Users> findUsersByRoleName(String roleName);


}
