package com.project.policyNews.service;

import com.project.policyNews.entity.Category;
import com.project.policyNews.entity.User;
import com.project.policyNews.model.CategoryDto;
import com.project.policyNews.repository.BookmarkRepository;
import com.project.policyNews.repository.CategoryRepository;
import com.project.policyNews.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class CategoryService {

  private final CategoryRepository categoryRepository;

  private final UserRepository userRepository;

  private final BookmarkRepository bookmarkRepository;

  public List<Category> getCategory(Long userId, String keyword) {
    return categoryRepository.findAllByUserIdAndKeyword(userId, keyword);
  }

  public Category registerCategory(CategoryDto category) {
    log.info("started to registerCategory");
    User user = userRepository.findById(category.getUserId())
        .orElseThrow(()-> new RuntimeException("유효하지 않은 사용자 입니다. "));

    if(categoryRepository.existsByNameAndUser_UserId(category.getName(), category.getUserId())) {
      throw new RuntimeException("이미 카테고리 등록된 명칭은 등록 불가합니다. ");
    }

    Category newCategory = new Category();
    newCategory.setName(category.getName());
    newCategory.setUser(user);
    categoryRepository.save(newCategory);
    log.info("end to registerCategory");

    return newCategory;
  }


  public Category modifyCategory(CategoryDto category) {
    boolean existYn = userRepository.existsById(category.getUserId());
    if(!existYn) {
      throw new RuntimeException("유효하지 않은 사용자 입니다. ");
    }

    Category categoryMod = categoryRepository.findById(category.getCateGoryId())
        .orElseThrow(() -> new RuntimeException("카테고리 내역을 찾을 수 없습니다. "));

    if(category.getName().equals(categoryMod.getName())) {
      throw new RuntimeException("변경전과 동일한 명칭으로는 변경 불가합니다. ");
    }

    if(categoryRepository.existsByNameAndUser_UserId(category.getName(), category.getUserId())) {
      throw new RuntimeException("이미 카테고리 등록된 명칭은 변경 불가합니다. ");
    }

    categoryMod.setName(category.getName());  //카테고리 명칭 변경
    categoryRepository.save(categoryMod);
    return categoryMod;
  }

  public void deleteCategory(Long cateGoryId, Long userId) {
    if(!userRepository.existsById(userId)) {
      throw new RuntimeException("유효하지 않은 사용자 입니다. ");
    }

    if(!categoryRepository.existsById(cateGoryId)){
      throw new RuntimeException("이미 존재하지 않는 카테고리 입니다. ");
    }

    // 카테고리에 즐겨찾기 항목이 있으면 삭제 불가처리
    if(bookmarkRepository.existsByCategory_CategoryId(cateGoryId)) {
      throw new RuntimeException("카테고리 하위에 즐겨찾기 기사가 있어 삭제 불가합니다.");
    }

    categoryRepository.deleteById(cateGoryId);
  }
}
