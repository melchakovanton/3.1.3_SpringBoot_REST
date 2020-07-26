package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Integer> {

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    List<User> findAll();

    void deleteById(Long id);
}
