package web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.repository.RoleRepo;

import java.util.List;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public List<Role> allEntity() {
        return roleRepo.findAll();
    }

    @Override
    public boolean addEntity(Role role) {
        roleRepo.save(role);
        return true;
    }

    @Override
    public boolean updateEntity(Role role) {
        if (getEntityById(role.getId()).getNameRole().equals(role.getNameRole()) || isRoleNameUnique(role)) {
            roleRepo.save(role);
        }
        return true;
    }

    @Override
    public void deleteEntity(Long id) {
        roleRepo.delete(roleRepo.findById(id));
    }

    @Override
    public Role getEntityById(Long id) {
        return roleRepo.findById(id);
    }

    @Override
    public Role getEntityByName(String name) {
        return roleRepo.findByNameRole(name).orElseThrow(() -> new IllegalStateException("User not find by name"));
    }

    private boolean isRoleNameUnique(Role role) {
        return !roleRepo.findByNameRole(role.getNameRole()).isPresent();
    }
}
