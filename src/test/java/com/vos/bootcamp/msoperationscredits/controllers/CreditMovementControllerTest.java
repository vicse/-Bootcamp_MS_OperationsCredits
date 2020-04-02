package com.vos.bootcamp.msoperationscredits.controllers;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CreditMovementControllerTest {

  @Mock
  private CreditMovementService creditMovementService;
  private WebTestClient client;
  private List<CreditMovement> expectedCreditMovements;

  private final CreditMovementType creditMovementType1 = CreditMovementType.builder().id("1").name("PAGOS").build();
  private final CreditMovementType creditMovementType2 = CreditMovementType.builder().id("2").name("RETIRO DE EFECTIVO").build();

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new CreditMovementController(creditMovementService))
            .configureClient()
            .baseUrl("/api/creditMovements")
            .build();

    expectedCreditMovements = Arrays.asList(
            CreditMovement.builder().id("1").accountNumberOrigin("1231-1311-12222")
                    .numDocOwner("75772936").amount(300.0).creditMovementType(creditMovementType1).build(),
            CreditMovement.builder().id("2").accountNumberOrigin("1231-1311-11111")
                    .numDocOwner("75772936").amount(200.0).creditMovementType(creditMovementType1).build(),
            CreditMovement.builder().id("3").accountNumberOrigin("1231-1311-12334")
                    .numDocOwner("76894512").amount(200.0).creditMovementType(creditMovementType2).build()
    );

  }

  @Test
  void getAll() {
    when(creditMovementService.findAll()).thenReturn(Flux.fromIterable(expectedCreditMovements));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(CreditMovement.class)
            .isEqualTo(expectedCreditMovements);
  }

  @Test
  void getCreditMovementById_whenCreditMovementExists_returnCorrectCreditMovement() {
    CreditMovement expectedCreditMovement = expectedCreditMovements.get(0);
    when(creditMovementService.findById(expectedCreditMovement.getId()))
            .thenReturn(Mono.just(expectedCreditMovement));

    client.get()
            .uri("/{id}", expectedCreditMovement.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(CreditMovement.class)
            .isEqualTo(expectedCreditMovement);
  }

  @Test
  void getCreditMovementById_whenCreditMovementNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(creditMovementService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addCreditMovement() {
    CreditMovement expectedCreditMovement = expectedCreditMovements.get(0);
    when(creditMovementService.save(expectedCreditMovement))
            .thenReturn(Mono.just(expectedCreditMovement));

    client.post()
            .uri("/")
            .body(Mono.just(expectedCreditMovement), CreditMovement.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CreditMovement.class)
            .isEqualTo(expectedCreditMovement);
  }

  @Test
  void updateCreditMovementType_whenCreditMovementTypeExists_performUpdate() {
    CreditMovement expectedCreditMovement = expectedCreditMovements.get(0);
    when(creditMovementService.update(expectedCreditMovement.getId(), expectedCreditMovement))
            .thenReturn(Mono.just(expectedCreditMovement));

    client.put()
            .uri("/{id}", expectedCreditMovement.getId())
            .body(Mono.just(expectedCreditMovement), CreditMovement.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CreditMovement.class)
            .isEqualTo(expectedCreditMovement);
  }

  @Test
  void updateCreditMovementType_whenCreditMovementTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    CreditMovement expectedCreditMovement = expectedCreditMovements.get(0);
    when(creditMovementService.update(id, expectedCreditMovement)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedCreditMovement), CreditMovement.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteCreditMovement_whenCreditMovementExists_performDeletion() {
    CreditMovement creditMovementToDelete = expectedCreditMovements.get(0);
    when(creditMovementService.deleteById(creditMovementToDelete.getId()))
            .thenReturn(Mono.just(creditMovementToDelete));

    client.delete()
            .uri("/{id}", creditMovementToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteCreditMovement_whenIdNotExist_returnNotFound() {
    CreditMovement creditMovementToDelete = expectedCreditMovements.get(0);
    when(creditMovementService.deleteById(creditMovementToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", creditMovementToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }


}
