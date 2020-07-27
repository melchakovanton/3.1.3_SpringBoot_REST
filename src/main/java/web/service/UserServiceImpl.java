package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dto.UserDto;
import web.model.Role;
import web.model.User;
import web.repository.UserRepo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> allEntity() {
        return userRepo.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean addEntity(UserDto userDto) {
        if (!isNameUnique(userDto)) {
            return false;
        }
        User user = fromForm(userDto);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean updateEntity(UserDto userDto) {
        if (getEntityById(userDto.getId()).getName().equals(userDto.getName()) || isNameUnique(userDto)) {
            User user = fromForm(userDto);
            if (user.getPassword().isEmpty()) {
                user.setPassword(getEntityById(user.getId()).getPassword());
            }
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void deleteEntity(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public UserDto getEntityById(Long id) {
        return new UserDto(userRepo.findById(id).orElseThrow(() -> new IllegalStateException("User not find by Id")));
    }

    @Override
    public UserDto getEntityByName(String name) throws IllegalStateException {
        return new UserDto(userRepo.findByName(name).orElseThrow(() -> new IllegalStateException("User not find by name")));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findByName(s).orElseThrow(() -> new IllegalStateException("User not find by name"));
    }

    private void setRoles(User user, UserDto userDto) {
        user.setRoles(Arrays.stream(userDto.getRoles())
                .map(roleService::getEntityByName)
                .collect(Collectors.toSet()));
    }

    private boolean isNameUnique(UserDto userDto) {
        return !userRepo.findByName(userDto.getName()).isPresent();
    }

    private User fromForm(UserDto userDto) {
        User user = new User(userDto);
        setRoles(user, userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }
}
