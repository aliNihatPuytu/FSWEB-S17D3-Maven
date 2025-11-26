package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Kangaroo;
import com.workintech.zoo.exceptions.ZooException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/kangaroos")
public class KangarooController {

    private final Map<Integer, Kangaroo> kangaroos = new ConcurrentHashMap<>();

    @GetMapping
    public ArrayList<Kangaroo> findAll() {
        return new ArrayList<>(kangaroos.values());
    }

    @GetMapping("/{id}")
    public Kangaroo findById(@PathVariable int id) {
        Kangaroo k = kangaroos.get(id);
        if (k == null) {
            throw new ZooException("Kangaroo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return k;
    }

    @PostMapping
    public Kangaroo save(@RequestBody Kangaroo kangaroo) {
        validateKangaroo(kangaroo);
        kangaroos.put(kangaroo.getId(), kangaroo);
        return kangaroo;
    }

    @PutMapping("/{id}")
    public Kangaroo update(@PathVariable int id, @RequestBody Kangaroo kangaroo) {
        if (!kangaroos.containsKey(id)) {
            throw new ZooException("Kangaroo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        validateKangaroo(kangaroo);
        kangaroo.setId(id);
        kangaroos.put(id, kangaroo);
        return kangaroo;
    }

    @DeleteMapping("/{id}")
    public Kangaroo delete(@PathVariable int id) {
        Kangaroo removed = kangaroos.remove(id);
        if (removed == null) {
            throw new ZooException("Kangaroo not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        return removed;
    }

    private void validateKangaroo(Kangaroo k) {
        if (k == null) throw new IllegalArgumentException("Kangaroo body is required");
        if (k.getId() <= 0) throw new IllegalArgumentException("Kangaroo id must be positive");
        if (k.getName() == null || k.getName().isBlank()) throw new IllegalArgumentException("Kangaroo name is required");
        if (k.getGender() == null || k.getGender().isBlank()) throw new IllegalArgumentException("Kangaroo gender is required");
        if (k.getHeight() <= 0) throw new IllegalArgumentException("Kangaroo height must be positive");
        if (k.getWeight() <= 0) throw new IllegalArgumentException("Kangaroo weight must be positive");
        if (k.getIsAggressive() == null) throw new IllegalArgumentException("Kangaroo isAggressive is required");
    }
}
