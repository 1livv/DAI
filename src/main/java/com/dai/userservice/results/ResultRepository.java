package com.dai.userservice.results;

import com.dai.userservice.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("Select t from results t where t.user.name=:name")
    List<Result> findAllByUser(@Param("name") String name);

    @EntityGraph(value = "")
    List<Result> findByAndUser(String name);
}
