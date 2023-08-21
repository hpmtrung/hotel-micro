package com.hba.reservationservice.domain.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

  private final DepartmentRepository departmentRepository;

  @Transactional(readOnly = true)
  public List<Department> getDepartments() {
    return departmentRepository.findAll();
  }
}