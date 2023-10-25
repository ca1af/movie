package com.example.movie.movie.repository;

import com.example.movie.movie.entity.Foo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface FooRepository extends JpaRepository<Foo, Long> {
    @Modifying
    @Query("UPDATE Foo f SET f.count = f.count + 1 WHERE f.id = :id")
    void addCount(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM FOO WHERE FOO.ID = :id FOR UPDATE")
    Foo getFooForUpdate(@Param("id") Long id);
}
