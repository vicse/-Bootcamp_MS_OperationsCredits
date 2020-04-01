package com.vos.bootcamp.msoperationscredits.services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementTypeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditMovementTypeServiceImpl implements CreditMovementTypeService {

  private final CreditMovementTypeRepository repository;

  public CreditMovementTypeServiceImpl(CreditMovementTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<CreditMovementType> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<CreditMovementType> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<CreditMovementType> save(CreditMovementType creditMovementType) {
    return repository.save(creditMovementType);
  }

  @Override
  public Mono<CreditMovementType> update(String id, CreditMovementType creditMovementType) {
    return repository.findById(id).flatMap(creditMovementTypeDB -> {

      if (creditMovementType.getName() == null) {
        creditMovementTypeDB.setName(creditMovementTypeDB.getName());
      } else {
        creditMovementTypeDB.setName(creditMovementType.getName());
      }

      return repository.save(creditMovementTypeDB);

    });
  }

  @Override
  public Mono<CreditMovementType> deleteById(String id) {
    return repository.findById(id)
            .flatMap(creditMovementType -> repository.delete(creditMovementType)
            .then(Mono.just(creditMovementType)));
  }
}
