package com.hba.roomservice.domains.room;

import org.springframework.beans.factory.annotation.Value;

public interface RentableSuiteRecordProjection {
  @Value("#{target.suite_id}")
  Integer getSuiteId();

  @Value("#{target.empty_room_num}")
  Integer getEmptyRoomNum();
}