package ru.practicum.repository.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

public interface IUserPrivateRepository extends JpaRepository<User, Long> {
}
