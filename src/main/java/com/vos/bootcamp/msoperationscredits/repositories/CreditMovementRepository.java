package com.vos.bootcamp.msoperationscredits.repositories;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditMovementRepository extends ReactiveMongoRepository<CreditMovement, String> {
}
