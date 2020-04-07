package com.vos.bootcamp.msoperationscredits.Services;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.models.CreditProduct;
import com.vos.bootcamp.msoperationscredits.models.CreditProductType;
import com.vos.bootcamp.msoperationscredits.repositories.CreditMovementRepository;
import com.vos.bootcamp.msoperationscredits.repositories.CreditProductRepository;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementService;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementServiceImpl;
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

  private final CreditProductType creditProductType = CreditProductType.builder().id("1").name("TARJETA DE CREDITO").build();

  private final CreditProduct creditProduct1 = CreditProduct.builder().numDocOwner("75772936")
          .accountNumber("1234-123123-123").creditAmount(1300.00).creditAmountAvailable(1300.00).creditProductType(creditProductType).build();

  private final CreditMovementType creditMovementType1 = CreditMovementType.builder().name("PAGOS").build();
  private final CreditMovementType creditMovementType2 = CreditMovementType.builder().name("RETIRO DE EFECTIVO").build();


  private final CreditMovement creditMovement1 = CreditMovement.builder().accountNumberOrigin("1234-123123-123")
          .numDocOwner("75772936").amount(300.0).creditMovementType(creditMovementType1).build();
  private final CreditMovement creditMovement2 = CreditMovement.builder().accountNumberOrigin("1234-123123-123")
          .numDocOwner("76894512").amount(200.0).creditMovementType(creditMovementType2).build();


  @Mock
  private CreditMovementRepository creditMovementRepository;

  @Mock
  private CreditProductRepository creditProductRepository;

  private CreditMovementService creditMovementService;

  @BeforeEach
  void SetUp(){
    creditMovementService = new CreditMovementServiceImpl(creditMovementRepository, creditProductRepository) {
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
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    when(creditMovementRepository.save(creditMovement1)).thenReturn(Mono.just(creditMovement1));

    Mono<CreditMovement> actual = creditMovementService.save(creditMovement1);

    assertResults(actual, creditMovement1);
  }

  @Test
  void create_whenAccountNumberNotExits() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(false));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    when(creditMovementRepository.save(creditMovement1)).thenReturn(Mono.just(creditMovement1));


    Mono<CreditMovement> actual = creditMovementService.save(creditMovement1);

    assertResults(actual, new Exception("This credit product not exist"));
  }

  @Test
  void create_whenCreditProductNotBelongsToCustomer() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    when(creditMovementRepository.save(creditMovement2)).thenReturn(Mono.just(creditMovement2));

    Mono<CreditMovement> actual = creditMovementService.save(creditMovement2);


    assertResults(actual, new Exception("This credit product does not belong to the client"));
  }

  @Test
  void create_whenCreditProductInsufficientBalance() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    creditProduct1.setCreditAmountAvailable(creditProduct1.getCreditAmountAvailable() - 1500.0);

    when(creditProductRepository.updateAmount(creditProduct1))
            .thenReturn(Mono.just(creditProduct1));

    when(creditMovementRepository.save(creditMovement1)).thenReturn(Mono.just(creditMovement1));

    Mono<CreditMovement> actual = creditMovementService.save(creditMovement1);

    assertResults(actual, new Exception("Insufficient balance"));
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

  /* ===============================================
           Validations of Credit Products
  ================================================== */
  @Test
  void validateCreditProduct_WhenCreditProductBelongsToCustomer_returnTrue() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    Mono<Boolean> actual = creditMovementService.validateCreditProduct(creditProduct1.getAccountNumber(), creditProduct1.getNumDocOwner());

    assertResults(actual, true);

  }

  @Test
  void validateBankAccount_WhenBankAccountNotExist_returnFalse() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(false));

    Mono<Boolean> actual = creditMovementService.validateCreditProduct(creditProduct1.getAccountNumber(), creditProduct1.getNumDocOwner());

    assertResults(actual, new Exception("This credit product not exist"));

  }

  @Test
  void validateBankAccount_WhenBankAccountNotBelongsToCustomer_returnFalse() {
    final String numDocCustomer = "NOT_EXIST";
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    Mono<Boolean> actual = creditMovementService.validateCreditProduct(creditProduct1.getAccountNumber(), numDocCustomer);

    assertResults(actual, new Exception("This credit product does not belong to the client"));

  }

  @Test
  void getCreditProductByAccountNumber_whenExist_returnCreditProduct() {
    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> creditProductMono = creditMovementService
            .findCreditProductByAccountNumber(creditMovement1.getAccountNumberOrigin());

    assertResults(creditProductMono, creditProduct1);

  }

  @Test
  void getCreditProductByAccountNumber_whenNotExist_returnEmpty() {
    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.empty());

    Mono<CreditProduct> creditProductMono = creditMovementService
            .findCreditProductByAccountNumber(creditMovement1.getAccountNumberOrigin());

    assertResults(creditProductMono);

  }

  @Test
  void getCreditProduct_WhenExits_returnTrue() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    Mono<Boolean> actual = creditMovementService.creditProductExits(creditProduct1.getAccountNumber());

    assertResults(actual, true);

  }

  @Test
  void getCreditProduct_WhenNotExits_returnFalse() {
    when(creditProductRepository.creditProductExits(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(false));

    Mono<Boolean> actual = creditMovementService.creditProductExits(creditProduct1.getAccountNumber());

    assertResults(actual, false);

  }

  @Test
  void updateAmountBankAccount_whenAccountNumberExists_returnUpdatedBankAccount() {
    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> actual = creditMovementService.updateAmountCreditProduct(creditProduct1.getAccountNumber(), 50.0);

      assertResults(actual, creditProduct1);
  }

  @Test
  void updateAmountBankAccount_whenAccountNumberNotExit_returnUpdatedBankAccount() {
    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.empty());

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> actual = creditMovementService.updateAmountCreditProduct(creditProduct1.getAccountNumber(), 50.0);

    assertResults(actual);
  }

  @Test
  void updateAmountBankAccount_whenInsufficientBalance_returnException() {
    when(creditProductRepository.findCreditProductByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> actual = creditMovementService.updateAmountCreditProduct(creditProduct1.getAccountNumber(), 1550.0);

    assertResultsCreditProduct(actual, new Exception("Insufficient balance"));
  }

  private void assertResults(Publisher<CreditMovement> publisher, CreditMovement... expectedCreditMovement) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCreditMovement)
            .verifyComplete();
  }

  private void assertResults(Publisher<CreditProduct> publisher, CreditProduct... expectedCreditProduct) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCreditProduct)
            .verifyComplete();
  }

  private void assertResultsCreditProduct(Publisher<CreditProduct> publisher, Exception exception) {
    StepVerifier
            .create(publisher)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResults(Publisher<CreditMovement> publisher, Exception exception) {
    StepVerifier
            .create(publisher)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResults(Mono<Boolean> actual, Exception exception) {
    StepVerifier
            .create(actual)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResults(Mono<Boolean> actual, boolean b) {
    StepVerifier
            .create(actual)
            .expectNext(b)
            .verifyComplete();
  }

}
