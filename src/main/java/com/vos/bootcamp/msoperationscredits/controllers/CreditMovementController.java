package com.vos.bootcamp.msoperationscredits.controllers;

import com.vos.bootcamp.msoperationscredits.models.CreditMovement;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/creditMovements")
@Api(value = "Bank Credits Movement Microservice")
public class CreditMovementController {

  private final CreditMovementService service;

  public CreditMovementController(CreditMovementService service) {
    this.service = service;
  }

  /* =========================================
   Function to List all Credit Products
 =========================================== */
  @GetMapping
  @ApiOperation(value = "List all CreditMovements", notes = "List all CreditMovements of Collections")
  public Flux<CreditMovement> getCreditMovements() {
    return service.findAll();
  }

  /* ===============================================
       Function to obtain a CreditMovement by id
  ============================================ */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a Credit Movement", notes = "Get a creditProduct by id")
  public Mono<ResponseEntity<CreditMovement>> getByIdCreditMovement(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
           Function to create a CreditMovement
 =============================================== */
  @PostMapping
  @ApiOperation(value = "Create Credit Movement", notes = "Create Credit Movement, check the model please")
  public Mono<ResponseEntity<CreditMovement>> createCreditMovement(
          @Valid @RequestBody CreditMovement creditMovement) {
    return service.save(creditMovement)
            .map(creditMovementDB -> ResponseEntity
                    .created(URI.create("/api/creditMovements/".concat(creditMovementDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(creditMovementDB)
            );
  }

  /* ====================================================
            Function to update a CreditMovement by id
    ===================================================== */
  @PutMapping("/{id}")
  @ApiOperation(value = "Update a Credit Movement", notes = "Update a credit product by ID")
  public Mono<ResponseEntity<CreditMovement>> updateCreditMovement(
          @PathVariable String id,
          @RequestBody CreditMovement creditMovement) {

    return service.update(id, creditMovement)
            .map(creditMovementDB -> ResponseEntity
                    .created(URI.create("/api/creditMovements/".concat(creditMovementDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(creditMovementDB))
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ====================================================
            Function to delete a CreditMovement by id
    ===================================================== */

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a Credit Movement", notes = "Delete a credit product by ID")
  public Mono<ResponseEntity<Void>> deleteByIdCreditMovement(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }


}
