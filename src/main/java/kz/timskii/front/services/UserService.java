package kz.timskii.front.services;

import kz.timskii.front.data.User;
import kz.timskii.front.data.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final AuthenticationFacade authenticationFacade;

    public UserService(UserRepository repository, AuthenticationFacade authenticationFacade) {
        this.repository = repository;
        this.authenticationFacade = authenticationFacade;
    }

    public Optional<User> get(UUID id) {
        return repository.findById(id);
    }

    public User save(User entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public void printCurrentUser() {
        String username = authenticationFacade.getCurrentUsername();
        System.out.println("Текущий пользователь: " + username);
    }
}
