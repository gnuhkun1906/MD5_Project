package ra.md5_project.service.IService;



import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;

import java.util.Optional;

public interface IRoleService {
    Optional<Roles> findByRoleName (ERole roleName);
    Optional<Roles> findRolesByRoleName(ERole name);


}
