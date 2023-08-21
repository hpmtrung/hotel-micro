package com.fs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class DeleteObjectsResponseDTO {

  List<String> notFoundKeys;

}