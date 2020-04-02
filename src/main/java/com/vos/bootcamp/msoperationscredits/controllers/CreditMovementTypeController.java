package com.vos.bootcamp.msoperationscredits.controllers;

import com.vos.bootcamp.msoperationscredits.models.CreditMovementType;
import com.vos.bootcamp.msoperationscredits.services.CreditMovementTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/creditMovements_Types")
@Api(value = "Bank Credits Movement Microservice")
public class CreditMovementTypeController {

  private final CreditMovementTypeService service;

  public CreditMovementTypeController(CreditMovementTypeService service) {
    this.service = service;
  }

  /* =========================================
    Function to List all Credit Products Types
  =========================================== */
  @GetMapping
  @ApiOperation(value = "List all CreditMovementTypes", notes = "List all CreditMovementTypes of Collections")
  public Flux<CreditMovementType> getCreditMovementTypes() {
    return service.findAll();
  }

  /* ===============================================
       Function to obtain a CreditMovementType by id
  ============================================ */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a Credit Movement Type", notes = "Get a creditProduct Type by id")
  public Mono<ResponseEntity<CreditMovementType>> getByIdCreditMovementType(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
           Function to create a CreditMovementType
 =============================================== */
  @PostMapping
  @ApiOperation(value = "Create Credit Movement Type", notes = "Create CreditMovementType, check the model please")
  public Mono<ResponseEntity<CreditMovementType>> createCreditMovementType(
          @Valid @RequestBody CreditMovementType CreditMovementType) {
    return service.save(CreditMovementType)
            .map(creditMovementTypeDB -> ResponseEntity
                    .created(URI.create("/api/creditMovements_Types/".concat(creditMovementTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(creditMovementTypeDB)
            );
  }

  /* ====================================================
            Function to update a CreditMovementType by id
    ===================================================== */
  @PutMapping("/{id}")
  @ApiOperation(value = "Update a CreditMovementType", notes = "Update a credit product Type by ID")
  public Mono<ResponseEntity<CreditMovementType>> updateCreditMovementType(
          @PathVariable String id,
          @RequestBody CreditMovementType CreditMovementType) {

    return service.update(id, CreditMovementType)
            .map(creditMovementTypeDB -> ResponseEntity
                    .created(URI.create("/api/creditMovements_Types/".concat(creditMovementTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(creditMovementTypeDB))
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ====================================================
            Function to delete a CreditMovementType by id
    ===================================================== */

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a CreditMovementType", notes = "Delete a credit product Type by ID")
  public Mono<ResponseEntity<Void>> deleteByIdCreditMovementType(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }



}
