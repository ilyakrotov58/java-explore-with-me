package ru.practicum.repository.pub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Compilation;

import java.util.List;

public interface ICompilationPublicRepository extends JpaRepository<Compilation, Long> {

    @Query(value =
            "SELECT * FROM compilations " +
                    "ORDER BY id " +
                    "LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Compilation> getCompilationList(
            int from,
            int size);


}
