package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserUsername(String userUsername);

    List<User> findByUserName(String userName);
}
