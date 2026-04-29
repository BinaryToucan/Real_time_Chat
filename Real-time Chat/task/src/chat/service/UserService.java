package chat.service;

import chat.user.User;
import chat.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /// Сохранение юзера
    public void save(User user) {
        repository.save(user);
    }

    /// Возвращает юзеров (только usernames)
    public List<String> getUsersNames() {
        return repository.findAll().stream()
                .map(User::getUsername)
                .toList();
    }

    @Transactional
    public void removeUser(String username) {
        repository.deleteByUsername(username);
    }

}

