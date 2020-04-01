package com.vos.bootcamp.msoperationscredits.repositories;
import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditMovementTypeRepository extends ReactiveMongoRepository<CreditMovementType, String> {
}
