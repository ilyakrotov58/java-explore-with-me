package ru.practicum.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;

import java.util.List;

public interface IUserAdminRepository extends JpaRepository<User, Long> {

    @Query(value =
            "SELECT * FROM users " +
                    "WHERE id IN (?1) " +
                    "LIMIT ?3 OFFSET ?2", nativeQuery = true)
    List<User> getUsers(int[] ids, int from, int size);

}
