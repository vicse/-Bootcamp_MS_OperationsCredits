package com.vos.bootcamp.msoperationscredits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsOperationsCreditsApplication {

  public static void main(String[] args) {
    SpringApplication.run(MsOperationsCreditsApplication.class, args);
  }

}
