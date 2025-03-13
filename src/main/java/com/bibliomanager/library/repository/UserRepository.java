package com.bibliomanager.library.repository;

import com.bibliomanager.library.model.User;
import com.bibliomanager.library.model.Review;
import com.bibliomanager.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByUserNameIgnoreCase(String userName);

    Optional<User> findByUserUsernameIgnoreCase(String username);

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId")
    List<Review> findReviewsByUser(@Param("userId") Long userId);

    @Query("SELECT DISTINCT r.book FROM Review r WHERE r.user.userId = :userId")
    List<Book> findBooksReviewedByUser(@Param("userId") Long userId);
}
