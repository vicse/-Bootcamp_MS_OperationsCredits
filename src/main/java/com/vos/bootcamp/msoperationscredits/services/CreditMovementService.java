package com.vos.bootcamp.msoperationscredits.services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import com.vos.bootcamp.msoperationscredits.utils.ICrud;
import reactor.core.publisher.Mono;

public interface CreditMovementService extends ICrud<CreditMovement> {

  public Mono<CreditProduct> findCreditProductByAccountNumber(String numAccount);

  public Mono<CreditProduct> updateAmountCreditProduct(String accountNumber, double amount);

  public Mono<Boolean> creditProductExits(String accountNumber);

  public Mono<Boolean> validateCreditProduct(String accountNumber, String numDocOwner);

}
