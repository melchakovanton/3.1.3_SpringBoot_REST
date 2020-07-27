package web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public interface ServiceAbstractInterface<T> {

    List<T> allEntity();

    boolean addEntity(T t);

    boolean updateEntity(T t);

    void deleteEntity(Long id);

    T getEntityById(Long id);

    T getEntityByName(String name);

}
