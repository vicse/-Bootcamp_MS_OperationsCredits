package com.vos.bootcamp.msoperationscredits.Services;


import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementTypeRepository;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeServiceImpl;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeService;
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
public class CreditMovementTypeServiceTest {

  private final CreditMovementType creditMovementType1 = CreditMovementType.builder().name("PAGOS").build();
  private final CreditMovementType creditMovementType2 = CreditMovementType.builder().name("RETIRO DE EFECTIVO").build();

  @Mock
  private CreditMovementTypeRepository creditMovementTypeRepository;

  private CreditMovementTypeService creditMovementTypeService;


  @BeforeEach
  void SetUp(){
    creditMovementTypeService = new CreditMovementTypeServiceImpl(creditMovementTypeRepository) {
    };
  }

  @Test
  void getAll() {
    when(creditMovementTypeRepository.findAll()).thenReturn(Flux.just(creditMovementType1, creditMovementType2));

    Flux<CreditMovementType> actual = creditMovementTypeService.findAll();

    assertResults(actual, creditMovementType1, creditMovementType2);
  }

  @Test
  void getById_whenIdExists_returnCorrectCreditMovementType() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.just(creditMovementType1));

    Mono<CreditMovementType> actual = creditMovementTypeService.findById(creditMovementType1.getId());

    assertResults(actual, creditMovementType1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovementType> actual = creditMovementTypeService.findById(creditMovementType1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(creditMovementTypeRepository.save(creditMovementType1)).thenReturn(Mono.just(creditMovementType1));

    Mono<CreditMovementType> actual = creditMovementTypeService.save(creditMovementType1);

    assertResults(actual, creditMovementType1);
  }

  @Test
  void update_whenIdExists_returnUpdatedCreditMovementType() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.just(creditMovementType1));
    when(creditMovementTypeRepository.save(creditMovementType1)).thenReturn(Mono.just(creditMovementType1));

    Mono<CreditMovementType> actual = creditMovementTypeService.update(creditMovementType1.getId(), creditMovementType1);

    assertResults(actual, creditMovementType1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovementType> actual = creditMovementTypeService.update(creditMovementType1.getId(), creditMovementType1);

    assertResults(actual);
  }

  @Test
  void delete_whenCreditMovementTypeExists_performDeletion() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.just(creditMovementType1));
    when(creditMovementTypeRepository.delete(creditMovementType1)).thenReturn(Mono.empty());

    Mono<CreditMovementType> actual = creditMovementTypeService.deleteById(creditMovementType1.getId());

    assertResults(actual, creditMovementType1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(creditMovementTypeRepository.findById(creditMovementType1.getId())).thenReturn(Mono.empty());

    Mono<CreditMovementType> actual = creditMovementTypeService.deleteById(creditMovementType1.getId());

    assertResults(actual);
  }

  private void assertResults(Publisher<CreditMovementType> publisher, CreditMovementType... expectedCreditType) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCreditType)
            .verifyComplete();
  }


}
