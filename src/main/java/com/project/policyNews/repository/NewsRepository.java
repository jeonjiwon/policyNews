package com.project.policyNews.repository;

import com.project.policyNews.entity.News;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

  boolean existsByStandardDate(LocalDate standardDate);


  @Query("SELECT n FROM News n WHERE n.standardDate = :standardDate AND (:keyword IS NULL OR n.title LIKE %:keyword%)")
  Page<News> findAllByStandardDateAndKeyword(@Param("standardDate") LocalDate standardDate, @Param("keyword") String keyword, Pageable pageable);

}
