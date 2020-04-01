package com.vos.bootcamp.msoperationscredits.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Document(collection = "ms_creditMovements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreditMovement {

  @Id
  private String id;

  @NotBlank(message = "'accountNumberOrigin' is required")
  private String accountNumberOrigin;

  private Double amount;

  private Date momentDate;




}
