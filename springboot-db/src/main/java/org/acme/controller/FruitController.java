package org.acme.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.acme.model.Fruit;
import org.acme.repository.FruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FruitController {
    
@Autowired
  FruitRepository fruitRepository;

  @GetMapping("/fruits")
  public ResponseEntity<List<Fruit>> getAllFruits(@RequestParam(required = false) String name) {
    try {
      List<Fruit> fruits = new ArrayList<Fruit>();

      if (name == null)
        fruitRepository.findAll().forEach(fruits::add);
      else
        fruitRepository.findByNameContaining(name).forEach(fruits::add);

      if (fruits.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(fruits, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/fruits/{id}")
  public ResponseEntity<Fruit> getFruitById(@PathVariable("id") long id) {
    Optional<Fruit> fruitData = fruitRepository.findById(id);

    if (fruitData.isPresent()) {
      return new ResponseEntity<>(fruitData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/fruits")
  public ResponseEntity<Fruit> createFruit(@RequestBody Fruit fruit) {
    try {
      Fruit _fruit = fruitRepository
          .save(new Fruit(fruit.getName()));
      return new ResponseEntity<>(_fruit, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/fruits/{id}")
  public ResponseEntity<Fruit> updateFruit(@PathVariable("id") long id, @RequestBody Fruit fruit) {
    Optional<Fruit> fruitData = fruitRepository.findById(id);

    if (fruitData.isPresent()) {
      Fruit _fruit = fruitData.get();
      _fruit.setName(fruit.getName());
      return new ResponseEntity<>(fruitRepository.save(_fruit), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/fruits/{id}")
  public ResponseEntity<HttpStatus> deleteFruit(@PathVariable("id") long id) {
    try {
      fruitRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/fruits")
  public ResponseEntity<HttpStatus> deleteAllFruits() {
    try {
      fruitRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
}
