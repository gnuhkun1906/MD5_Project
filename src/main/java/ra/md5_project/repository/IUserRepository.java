package ra.md5_project.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ra.md5_project.model.Users;

import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Users findByUserName(String userName);
    Page<Users> findAll(Pageable pageable);
//    List<Users> findUsersByRoleName(String roleName);


}
