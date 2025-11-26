package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Koala;
import com.workintech.zoo.exceptions.ZooException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/koalas")
public class KoalaController {

    private final Map<Integer, Koala> koalas = new ConcurrentHashMap<>();

    @GetMapping
    public ArrayList<Koala> findAll() {
        return new ArrayList<>(koalas.values());
    }

    @GetMapping("/{id}")
    public Koala findById(@PathVariable int id) {
        Koala k = koalas.get(id);
        if (k == null) {
            throw new ZooException("Koala not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return k;
    }

    @PostMapping
    public Koala save(@RequestBody Koala koala) {
        validateKoala(koala);
        koalas.put(koala.getId(), koala);
        return koala;
    }

    @PutMapping("/{id}")
    public Koala update(@PathVariable int id, @RequestBody Koala koala) {
        if (!koalas.containsKey(id)) {
            throw new ZooException("Koala not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        validateKoala(koala);
        koala.setId(id);
        koalas.put(id, koala);
        return koala;
    }

    @DeleteMapping("/{id}")
    public Koala delete(@PathVariable int id) {
        Koala removed = koalas.remove(id);
        if (removed == null) {
            throw new ZooException("Koala not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return removed;
    }

    private void validateKoala(Koala k) {
        if (k == null) throw new IllegalArgumentException("Koala body is required");
        if (k.getId() <= 0) throw new IllegalArgumentException("Koala id must be positive");
        if (k.getName() == null || k.getName().isBlank()) throw new IllegalArgumentException("Koala name is required");
        if (k.getGender() == null || k.getGender().isBlank()) throw new IllegalArgumentException("Koala gender is required");
        if (k.getSleepHour() <= 0) throw new IllegalArgumentException("Koala sleepHour must be positive");
        if (k.getWeight() <= 0) throw new IllegalArgumentException("Koala weight must be positive");
    }
}
