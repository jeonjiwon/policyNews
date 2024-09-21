package com.project.policyNews.repository;

import com.project.policyNews.entity.Bookmark;
import com.project.policyNews.model.HotNewsDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {


  @Query("SELECT b FROM Bookmark b JOIN b.category c JOIN c.user u JOIN b.news a WHERE b.category.categoryId = :categoryId")
  List<Bookmark> findAllByCategoryId(@Param("categoryId") Long categoryId);

  @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
      "FROM Bookmark b " +
      "JOIN b.category c " +
      "JOIN c.user u " +
      "JOIN b.news a " +
      "WHERE u.userId = :userId "
      +"AND a.id = :newsId")
  boolean existsByUserIdAndNewsId(@Param("userId") Long userId, @Param("newsId") Long newsId);


  boolean existsByCategory_CategoryId(Long categoryId);

  @Query("SELECT new com.project.policyNews.model.HotNewsDto(COUNT(b) AS bookmarkCount, a.id, a.title, a.articleUrl ) " +
      "FROM Bookmark b " +
      "JOIN b.news a " +
      "WHERE a.standardDate = :searchDate " +
      "GROUP BY a.id, a.title, a.articleUrl, b.tags " +
      "ORDER BY COUNT(b) DESC")
  List<HotNewsDto> findHotNewsWithTagsAndSearchDate(@Param("searchDate") LocalDate searchDate, Pageable pageable);
}
