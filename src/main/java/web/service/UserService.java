package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.dto.UserDto;

public interface UserService extends ServiceAbstractInterface<UserDto>, UserDetailsService {

}
