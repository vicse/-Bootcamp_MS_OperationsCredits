package com.vos.bootcamp.msoperationscredits.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "ms_creditMovements_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreditMovementType {

  @Id
  private String id;

  @NotBlank(message = "'Names' are required")
  private String name;
}
