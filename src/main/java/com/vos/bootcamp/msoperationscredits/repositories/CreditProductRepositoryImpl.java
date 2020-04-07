package com.vos.bootcamp.msoperationscredits.repositories;

import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class CreditProductRepositoryImpl implements CreditProductRepository {

  private WebClient webClient = WebClient.create("http://localhost:8003/api/creditProducts");

  @Override
  public Mono<CreditProduct> findCreditProductByAccountNumber(String accountNumber) {
    return webClient
            .get()
            .uri("/accountNumber/" + accountNumber)
            .retrieve()
            .bodyToMono(CreditProduct.class)
            ;
  }

  @Override
  public Mono<Boolean> creditProductExits(String accountNumber) {
    return webClient
            .get()
            .uri("/" + accountNumber + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class)
            ;
  }

  @Override
  public Mono<CreditProduct> updateAmount(CreditProduct creditProduct) {
    return webClient
            .put()
            .uri("/" + creditProduct.getId())
            .body(Mono.just(creditProduct), CreditProduct.class)
            .retrieve()
            .bodyToMono(CreditProduct.class)
            ;
  }

}
