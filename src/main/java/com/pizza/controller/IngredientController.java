package com.pizza.controller;

import com.pizza.model.dto.IngredientDto;
import com.pizza.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping()
    @Operation(summary = "Retrieve all ingredients")
    @ApiResponse(responseCode = "200", description = "List of all ingredients",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "[{'id': 1, 'name': 'Peanut Butter', 'stock': 100, 'allergens': 'Nuts'}, {'id': 2, 'name': 'Cheese', 'stock': 50, 'allergens': 'Dairy'}]")))
    public List<IngredientDto> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve ingredient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'id': 1, 'name': 'Peanut Butter', 'stock': 100, 'allergens': 'Nuts'}"))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found")
    })
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable(value = "id") Long id) throws Exception {
        return ingredientService.getIngredientById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Create ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient created",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'name': 'Olive', 'stock': 80, 'allergens': 'Olives'}"))),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody @Valid final IngredientDto ingredientDtoRequest) {
        IngredientDto ingredientDtoResponse = ingredientService.saveIngredient(ingredientDtoRequest);
        return ResponseEntity.ok(ingredientDtoResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ingredients name,stock,allergens by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredient updated",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'name': 'Olive', 'stock': 60, 'allergens': 'Olives'}"))),
            @ApiResponse(responseCode = "404", description = "Ingredient not found"),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable(value = "id") Long id, @RequestBody @Valid IngredientDto newIngredientDto) {
        return ingredientService.updateIngredient(id, newIngredientDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ingredient by id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "404", description = "Ingredient not found")
            })
    public ResponseEntity<Void> deleteIngredient(@PathVariable(value = "id") Long id) {

        boolean deleted = ingredientService.deleteIngredientById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
