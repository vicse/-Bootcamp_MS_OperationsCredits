package com.vos.bootcamp.msoperationscredits.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditMovementTypeRepository extends ReactiveMongoRepository<CreditMovementTypeRepository, String> {
}
