package com.hba.apigateway.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hba.apigateway.proxies.DepartmentDTO;
import com.hba.apigateway.proxies.HServiceDTO;
import com.hba.apigateway.proxies.SuiteStyleDTO;
import com.hba.apigateway.proxies.SuiteTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.util.function.Tuple4;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonPayload {
  private List<SuiteTypeDTO> suiteTypes;
  private List<SuiteStyleDTO> suiteStyles;
  private List<DepartmentDTO> departments;
  private List<HServiceDTO> services;

  public static CommonPayload create(Tuple4<List<SuiteStyleDTO>, List<SuiteTypeDTO>, List<DepartmentDTO>, List<HServiceDTO>> tuple ) {
    return new CommonPayload(tuple.getT2(), tuple.getT1(), tuple.getT3(), tuple.getT4());
  }
}