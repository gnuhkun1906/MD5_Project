package ra.md5_project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName (ERole roleName);
    Optional<Roles> findRolesByRoleName(ERole name);

}
