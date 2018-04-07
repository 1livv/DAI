package com.dai.userservice.results;

import com.dai.userservice.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findAllByUser(User user);

    @EntityGraph(value = "")
    List<Result> findByAndUser(String name);
}
