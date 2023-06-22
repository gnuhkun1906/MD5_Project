package ra.md5_project.service.serviceIPM;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.md5_project.model.ERole;
import ra.md5_project.model.Roles;
import ra.md5_project.repository.IRoleRepository;
import ra.md5_project.service.IService.IRoleService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Optional<Roles> findRolesByRoleName(ERole name) {
        return roleRepository.findRolesByRoleName(name);
    }
}
