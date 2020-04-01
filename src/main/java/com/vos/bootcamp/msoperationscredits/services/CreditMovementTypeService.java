package com.vos.bootcamp.msoperationscredits.services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditMovementTypeService {

  public Flux<CreditMovementType> findAll();

  public Mono<CreditMovementType> findById(String id);

  public Mono<CreditMovementType> save(CreditMovementType creditMovementType);

  public Mono<CreditMovementType> update(String id, CreditMovementType creditMovementType);

  public Mono<CreditMovementType> deleteById(String id);

}
