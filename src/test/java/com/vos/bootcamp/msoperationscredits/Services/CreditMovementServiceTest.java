package com.vos.bootcamp.msoperationscredits.Services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementRepository;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementTypeRepository;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementService;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementServiceImpl;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeService;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CreditMovementServiceTest {

  private final CreditMovementType creditMovementType1 = CreditMovementType.builder().name("PAGOS").build();
  private final CreditMovementType creditMovementType2 = CreditMovementType.builder().name("RETIRO DE EFECTIVO").build();

  private final CreditMovement creditMovement1 = CreditMovement.builder().accountNumberOrigin("1231-1311-12333")
          .numDocOwner("75772936").amount(300.0).creditMovementType(creditMovementType1).build();
  private final CreditMovement creditMovement2 = CreditMovement.builder().accountNumberOrigin("1231-1311-12323")
          .numDocOwner("75772936").amount(200.0).creditMovementType(creditMovementType2).build();

  @Mock
  private CreditMovementRepository creditMovementRepository;

  private CreditMovementService creditMovementService;

  @BeforeEach
  void SetUp(){
    creditMovementService = new CreditMovementServiceImpl(creditMovementRepository) {
    };
  }

  @Test
  void getAll() {
    when(creditMovementRepository.findAll()).thenReturn(Flux.just(creditMovement1, creditMovement2));

    Flux<CreditMovement> actual = creditMovementService.findAll();

    assertResults(actual, creditMovement1, creditMovement2);
  }

  @Test
  void getById_whenIdExists_returnCorrectCreditMovement() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.just(creditMovement1));

    Mono<CreditMovement> actual = creditMovementService.findById(creditMovement1.getId());

    assertResults(actual, creditMovement1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovement> actual = creditMovementService.findById(creditMovement1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(creditMovementRepository.save(creditMovement1)).thenReturn(Mono.just(creditMovement1));

    Mono<CreditMovement> actual = creditMovementService.save(creditMovement1);

    assertResults(actual, creditMovement1);
  }

  @Test
  void update_whenIdExists_returnUpdatedCreditMovement() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.just(creditMovement1));
    when(creditMovementRepository.save(creditMovement1)).thenReturn(Mono.just(creditMovement1));

    Mono<CreditMovement> actual = creditMovementService.update(creditMovementType1.getId(), creditMovement1);

    assertResults(actual, creditMovement1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovement> actual = creditMovementService.update(creditMovement1.getId(), creditMovement1);

    assertResults(actual);
  }



  @Test
  void delete_whenCreditMovementExists_performDeletion() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.just(creditMovement1));
    when(creditMovementRepository.delete(creditMovement1)).thenReturn(Mono.empty());

    Mono<CreditMovement> actual = creditMovementService.deleteById(creditMovement1.getId());

    assertResults(actual, creditMovement1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(creditMovementRepository.findById(creditMovement1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovement> actual = creditMovementService.deleteById(creditMovement1.getId());

    assertResults(actual);
  }


  private void assertResults(Publisher<CreditMovement> publisher, CreditMovement... expectedCreditMovement) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCreditMovement)
            .verifyComplete();
  }



}
