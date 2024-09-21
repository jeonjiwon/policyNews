package com.project.policyNews.repository;

import com.project.policyNews.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("SELECT c FROM Category c JOIN c.user u WHERE c.user.userId = :userId AND (:keyword IS NULL OR c.name LIKE %:keyword%)")
  List<Category> findAllByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

  boolean existsByNameAndUser_UserId(String name, Long userId);
}
