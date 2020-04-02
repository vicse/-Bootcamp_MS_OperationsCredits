package com.vos.bootcamp.msoperationscredits.controllers;

import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeService;
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
public class CreditMovementTypeControllerTest {

  @Mock
  private CreditMovementTypeService creditMovementTypeService;
  private WebTestClient client;
  private List<CreditMovementType> expectedCreditMovementTypes;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new CreditMovementTypeController(creditMovementTypeService))
            .configureClient()
            .baseUrl("/api/creditMovements_Types")
            .build();

    expectedCreditMovementTypes = Arrays.asList(
            CreditMovementType.builder().id("1").name("PAGOS").build(),
            CreditMovementType.builder().id("2").name("RETIRO EFECTIVO").build());

  }

  @Test
  void getAll() {
    when(creditMovementTypeService.findAll()).thenReturn(Flux.fromIterable(expectedCreditMovementTypes));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(CreditMovementType.class)
            .isEqualTo(expectedCreditMovementTypes);
  }

  @Test
  void getCreditMovementTypeById_whenCreditMovementTypeExists_returnCorrectCreditMovementType() {
    CreditMovementType expectedCreditMovementType = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.findById(expectedCreditMovementType.getId()))
            .thenReturn(Mono.just(expectedCreditMovementType));

    client.get()
            .uri("/{id}", expectedCreditMovementType.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(CreditMovementType.class)
            .isEqualTo(expectedCreditMovementType);
  }

  @Test
  void getCreditMovementTypeById_whenCreditMovementTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(creditMovementTypeService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addCreditMovementType() {
    CreditMovementType expectedCreditMovementType = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.save(expectedCreditMovementType))
            .thenReturn(Mono.just(expectedCreditMovementType));

    client.post()
            .uri("/")
            .body(Mono.just(expectedCreditMovementType), CreditMovementType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CreditMovementType.class)
            .isEqualTo(expectedCreditMovementType);
  }

  @Test
  void updateCreditMovementType_whenCreditMovementTypeExists_performUpdate() {
    CreditMovementType expectedCreditMovementType = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.update(expectedCreditMovementType.getId(), expectedCreditMovementType))
            .thenReturn(Mono.just(expectedCreditMovementType));

    client.put()
            .uri("/{id}", expectedCreditMovementType.getId())
            .body(Mono.just(expectedCreditMovementType), CreditMovementType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CreditMovementType.class)
            .isEqualTo(expectedCreditMovementType);
  }

  @Test
  void updateCreditMovementType_whenCreditMovementTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    CreditMovementType expectedCreditMovementType = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.update(id, expectedCreditMovementType)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedCreditMovementType), CreditMovementType.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteCreditMovementType_whenCreditMovementTypeExists_performDeletion() {
    CreditMovementType creditMovementTypeToDelete = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.deleteById(creditMovementTypeToDelete.getId()))
            .thenReturn(Mono.just(creditMovementTypeToDelete));

    client.delete()
            .uri("/{id}", creditMovementTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteCreditMovementType_whenIdNotExist_returnNotFound() {
    CreditMovementType creditMovementTypeToDelete = expectedCreditMovementTypes.get(0);
    when(creditMovementTypeService.deleteById(creditMovementTypeToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", creditMovementTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }


}
