package com.project.policyNews.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanLister implements CommandLineRunner {

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public void run(String... args) {
    // Bean 등록 확인
    String[] beanNames = applicationContext.getBeanDefinitionNames();
    for (String beanName : beanNames) {
      System.out.println(beanName);
    }
    System.out.println("===================================================");
    System.out.println("================= Server Started ==================");
    System.out.println("===================================================");
  }
}