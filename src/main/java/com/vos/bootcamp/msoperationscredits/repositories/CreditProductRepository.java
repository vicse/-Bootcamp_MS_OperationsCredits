package com.vos.bootcamp.msoperationscredits.repositories;

import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import reactor.core.publisher.Mono;

public interface CreditProductRepository {

  public Mono<CreditProduct> findCreditProductByAccountNumber(String accountNumber);

  public Mono<Boolean> creditProductExits(String accountNumber);

  public Mono<CreditProduct> updateAmount(CreditProduct creditProduct);

}
