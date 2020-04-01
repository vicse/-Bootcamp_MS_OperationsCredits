package com.vos.bootcamp.msoperationscredits.services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CreditMovementServiceImpl implements CreditMovementService {

  private final CreditMovementRepository repository;

  public CreditMovementServiceImpl(CreditMovementRepository repository) {
    this.repository = repository;
  }

  @Override
  public Mono<CreditProduct> findCreditProductByAccountNumber(String accountNumber) {
    return WebClient
            .create()
            .get()
            .uri("http://localhost:8003/api/creditProducts/accountNumber/" + accountNumber)
            .retrieve()
            .bodyToMono(CreditProduct.class);

  }

  @Override
  public Mono<CreditProduct> updateAmountCreditProduct(String accountNumber, double amount) {

    return this.findCreditProductByAccountNumber(accountNumber)
      .flatMap(creditProduct -> {

        if (amount > creditProduct.getCreditAmountAvailable()) {
          return Mono.error(new Exception("insufficient balance"));
        } else {
          creditProduct.setCreditAmountAvailable(creditProduct.getCreditAmountAvailable() - amount);
          creditProduct.setUpdatedAt(new Date());
          return WebClient
                  .create()
                  .put()
                  .uri("http://localhost:8003/api/creditProducts/" + creditProduct.getId())
                  .body(Mono.just(creditProduct), CreditProduct.class)
                  .retrieve()
                  .bodyToMono(CreditProduct.class);
        }

      });
  }

  @Override
  public Mono<Boolean> validateCreditAccount(String accountNumber, String numDocOwner) {

    Mono<CreditProduct> creditProductMono = findCreditProductByAccountNumber(accountNumber);

    return creditProductMono.flatMap(creditProduct -> {

      if (creditProduct == null) {
        return Mono.error(new Exception("This credit product not exist"));
      } else {
        if (creditProduct.getNumDocOwner().equals(numDocOwner)) {
          return Mono.just(true);
        } else {
          return Mono.error(new Exception("This credit product does not belong to the client"));
        }
      }

    });
  }

  @Override
  public Flux<CreditMovement> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<CreditMovement> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<CreditMovement> save(CreditMovement creditMovement) {

    Mono<Boolean> responseValidateCreditProduct = validateCreditAccount(
            creditMovement.getAccountNumberOrigin(),
            creditMovement.getNumDocOwner());

    return responseValidateCreditProduct.flatMap(resp -> {

      if (resp) {
        return this.updateAmountCreditProduct(creditMovement.getAccountNumberOrigin(), creditMovement.getAmount())
                .then(repository.save(creditMovement));
      } else {
        return Mono.error(new Exception("Error saving credit movement"));
      }

    });

  }

  @Override
  public Mono<CreditMovement> update(String id, CreditMovement updatedCreditMovement) {
    return repository.findById(id)
            .map(creditMovementDB -> creditMovementDB.toBuilder()
                    .amount(updatedCreditMovement.getAmount())
                    .creditMovementType(updatedCreditMovement.getCreditMovementType())
                    .build())
            .flatMap(repository::save);

  }

  @Override
  public Mono<CreditMovement> deleteById(String id) {
    return repository.findById(id).flatMap(creditMovement -> repository.delete(creditMovement)
            .then(Mono.just(creditMovement)));
  }
}
