package com.project.policyNews.repository;

import com.project.policyNews.entity.Bookmark;
import com.project.policyNews.model.BookmarkDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  @Query("SELECT new com.project.policyNews.model.BookmarkDto(b.category.categoryId, c.name, u.userId, b.id, a.id, a.title, b.tags) " +
      "FROM Bookmark b " +
      "JOIN b.category c " +
      "JOIN c.user u " +
      "JOIN b.news a " +
      "WHERE b.category.categoryId = :categoryId")
  List<BookmarkDto> findAllByCategoryIdWithDetails(@Param("categoryId") Long categoryId);

  @Query("SELECT new com.project.policyNews.model.BookmarkDto(b.category.categoryId, c.name, u.userId, b.id, a.id, a.title, b.tags) " +
      "FROM Bookmark b " +
      "JOIN b.category c " +
      "JOIN c.user u " +
      "JOIN b.news a " +
      "WHERE u.userId = :userId AND a.id = :id")
  List<BookmarkDto> findByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);
}
