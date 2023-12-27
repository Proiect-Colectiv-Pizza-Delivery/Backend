package com.pizza.controller;

import com.pizza.model.dto.IngredientDto;
import com.pizza.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable(value = "id") Long id) throws Exception {
        return ingredientService.getIngredientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody @Valid final IngredientDto ingredientDtoRequest) {
        IngredientDto ingredientDtoResponse = ingredientService.saveIngredient(ingredientDtoRequest);
        return ResponseEntity.ok(ingredientDtoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable(value = "id") Long id, @RequestBody @Valid IngredientDto newIngredientDto) {
        return ingredientService.updateIngredient(id, newIngredientDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable(value = "id") Long id) {

        boolean deleted = ingredientService.deleteIngredientById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
