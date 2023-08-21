package com.hba.apigateway.partners;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CredentialDTO {
  @NotBlank String email;
  @NotBlank String password;
}