package com.pizza.controller;

import com.pizza.exception.CustomException;
import com.pizza.model.dto.*;
import com.pizza.service.PizzaIngredientService;
import com.pizza.service.PizzaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    private final PizzaIngredientService pizzaIngredientService;

    @Autowired
    public PizzaController(PizzaService pizzaService, PizzaIngredientService pizzaIngredientService) {
        this.pizzaService = pizzaService;
        this.pizzaIngredientService = pizzaIngredientService;
    }

    @GetMapping()
    @Operation(summary = "Retrieve all pizzas")
    @ApiResponse(responseCode = "200", description = "List of all pizzas",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "[{'id': 1, 'name': 'Margherita', 'allergens': 'Tomatoes, Cheese', 'price': 9.99, 'blatType': 'Thin', 'blatQuantity': 1, 'baseName': 'Tomato', 'baseQuantity': 1}]")))
    public List<PizzaDto> getAllPizzas() {
        return pizzaService.getAllPizzas();
    }

    @GetMapping("/ingredients")
    @Operation(summary = "Retrieve all pizzas with their ingredients")
    @ApiResponse(responseCode = "200", description = "List of all pizzas with ingredients",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "[{'id': 2, 'name': 'Hawaiian', 'allergens': 'Pineapple, Ham', 'price': 11.99, 'blatType': 'Thick', 'blatQuantity': 2, 'baseName': 'Cheese', 'baseQuantity': 2, 'ingredients': [{'id': 1, 'name': 'Peanut Butter', 'stock': 100, 'allergens': 'Nuts'}, {'id': 2, 'name': 'Cheese', 'stock': 50, 'allergens': 'Dairy'}]}]")))
    public List<PizzaDtoWithIngredients> getAllPizzasWithIngredients() {
        return pizzaService.getAllPizzasWithIngredients();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve pizza by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'id': 1, 'name': 'Margherita', 'allergens': 'Tomatoes, Cheese', 'price': 9.99, 'blatType': 'Thin', 'blatQuantity': 1, 'baseName': 'Tomato', 'baseQuantity': 1}"))),
            @ApiResponse(responseCode = "404", description = "Pizza not found")
    })
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable(value = "id") Long id) throws Exception {
        return pizzaService.getPizzaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}/ingredients")
    @Operation(summary = "Retrieve pizza with its ingredients by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza with ingredients found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'id': 2, 'name': 'Hawaiian', 'allergens': 'Pineapple, Ham', 'price': 11.99, 'blatType': 'Thick', 'blatQuantity': 2, 'baseName': 'Cheese', 'baseQuantity': 2, 'ingredients': [{'ingredientId': 1, 'quantity': 2}]}"))),
            @ApiResponse(responseCode = "404", description = "Pizza not found")
    })
    public ResponseEntity<PizzaDtoWithIngredients> getPizzaWithIngredientsById(@PathVariable(value = "id") Long id) throws Exception {
        return pizzaService.getPizzaWithIngredientsById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    @Operation(summary = "Create pizza")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pizza created",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'id': 3, 'name': 'Pepperoni', 'allergens': 'Pepperoni, Cheese', 'price': 10.99, 'blatType': 'Medium', 'blatQuantity': 1, 'baseName': 'Cheese', 'baseQuantity': 1}"))),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<PizzaDto> createPizza(@RequestBody @Valid final PizzaDto pizzaDtoRequest) {
        PizzaDto pizzaDtoResponse = pizzaService.savePizza(pizzaDtoRequest);
        return ResponseEntity.ok(pizzaDtoResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pizza name,allergens,price,blatType,blatQuantity,baseName,baseQuantity fields by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza updated",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'id': 1, 'name': 'Veggie', 'allergens': 'Bell Peppers, Olives', 'price': 12.99, 'blatType': 'Thick', 'blatQuantity': 2, 'baseName': 'Tomato', 'baseQuantity': 1}"))),
            @ApiResponse(responseCode = "404", description = "Pizza not found"),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<PizzaDto> updatePizza(@PathVariable(value = "id") Long id, @RequestBody @Valid PizzaDto newPizzaDto) {
        return pizzaService.updatePizza(id, newPizzaDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pizza by id")
    @ApiResponses(value =
            {
                    @ApiResponse(responseCode = "404", description = "Pizza not found")
            })
    public ResponseEntity<Void> deletePizza(@PathVariable(value = "id") Long id) {
        boolean deleted = pizzaService.deletePizzaById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updateIngredients")
    @Operation(summary = "Update pizza ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza ingredients updated",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'pizzaId': 1, 'ingredientsIdList': [{'ingredientId': 3, 'quantity': 2}]}"))),
            @ApiResponse(responseCode = "404", description = "Pizza or ingredient not found"),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<PizzaIngredientIdsWithQuantityList> updatePizzaIngredients(
            @RequestBody @Valid PizzaIngredientIdsWithQuantityList pizzaIngredientIdsWithQuantityList) throws CustomException {
        PizzaIngredientIdsWithQuantityList returnedPizzaIngredientsList = pizzaIngredientService.updatePizzaIngredients(pizzaIngredientIdsWithQuantityList);
        return ResponseEntity.ok(returnedPizzaIngredientsList);
    }

    @DeleteMapping("/removeIngredients")
    @Operation(summary = "Remove pizza ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ingredients removed from pizza",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{'pizzaId': 1, 'ingredientsList': [{'ingredientId': 3, 'quantity': 0}]}"))),
            @ApiResponse(responseCode = "404", description = "Pizza or ingredient not found"),
            @ApiResponse(responseCode = "400", description = "Validation errors")
    })
    public ResponseEntity<PizzaIngredientWithQuantityList> removeIngredientsFromPizza(
            @RequestBody @Valid PizzaIngredientList pizzaIngredientList) throws CustomException {
        return ResponseEntity.ok(pizzaIngredientService.removePizzaIngredientsAndReturn(pizzaIngredientList));
    }

}
