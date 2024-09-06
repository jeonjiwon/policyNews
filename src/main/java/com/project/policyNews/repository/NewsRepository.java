package com.project.policyNews.repository;

import com.project.policyNews.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {

  boolean existsByStandardDate(String standardDate);


  @Query("SELECT n FROM News n WHERE n.standardDate = :standardDate AND (:keyword IS NULL OR n.title LIKE %:keyword%)")
  Page<News> findAllByStandardDateAndKeyword(@Param("standardDate") String standardDate, @Param("keyword") String keyword, Pageable pageable);

}
