package com.vos.bootcamp.msoperationscredits.services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditMovementService {

  public Flux<CreditMovement> findAll();

  public Mono<CreditMovement> findById(String id);

  public Mono<CreditMovement> save(CreditMovement creditMovement);

  public Mono<CreditMovement> update(String id, CreditMovement creditMovement);

  public Mono<CreditMovement> deleteById(String id);

  public Mono<CreditProduct> findCreditProductByAccountNumber(String numAccount);

  public Mono<CreditProduct> updateAmountCreditProduct(String accountNumber, double amount);

  public Mono<Boolean> validateCreditAccount(String accountNumber, String numDocOwner);

}
