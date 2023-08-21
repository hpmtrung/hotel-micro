package com.hba.roomservice.mongodb;

import com.mongodb.client.MongoClients;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MongoDBTest {

  MongoTemplate template =
      new MongoTemplate(MongoClients.create("mongodb://192.168.1.7:27017/"), "roomdb");

  @Test
  void testInsertRecord() {
    template.insert(new Suite(1, "deluxe"));

    Suite suite = template.findOne(new Query(Criteria.where("name").is("deluxe")), Suite.class);
    assertThat(suite).isNotNull();
    assertThat(suite.getId()).isEqualTo(1);
  }

  @Test
  @DisplayName("Test findAndModify operation")
  void testFindAndModifyOperation() {
    prepareData();

    Query query = new Query(Criteria.where("id").is(1));
    Update update = new Update().set("name", "dubo");

    Suite oldValue =
        template
            .update(Suite.class)
            .matching(query)
            .apply(update)
            .findAndModifyValue(); // return the old

    assertThat(oldValue.getName()).isEqualTo("deluxe");
    assertThat(oldValue.getId()).isEqualTo(1);

    Suite newValue = template.query(Suite.class).matching(query).oneValue();

    assertThat(newValue.getName()).isEqualTo("dubo");

    update = new Update().set("name", "second_dubo");

    Suite newest =
        template
            .update(Suite.class)
            .matching(query)
            .apply(update)
            .withOptions(FindAndModifyOptions.options().returnNew(true))
            .findAndModifyValue();

    assertThat(newest.getName()).isEqualTo("second_dubo");
  }

  @Test
  @DisplayName("Test OptimisticLockException")
  void testOptimisticLockException() {
    Person bob = template.insert(new Person("Bob"));
    Person alex = template.findOne(Query.query(Criteria.where("id").is(bob.getId())), Person.class);

    bob.setName("New bob");
    template.save(bob);

    alex.setName("Alex");
    Assertions.assertThrows(
        OptimisticLockingFailureException.class,
        () -> {
          template.save(alex);
        });
  }

  private void prepareData() {
    List<Suite> suites = List.of(new Suite(1, "deluxe"), new Suite(2, "Box"), new Suite(3, "Luu"));
    suites.forEach(template::save);
  }

  @Data
  @AllArgsConstructor
  private static class Suite {
    int id;
    String name;
  }

  @Data
  private static class Person {
    String id;
    String name;
    @Version Long version;

    public Person(String name) {
      this.name = name;
    }
  }
}