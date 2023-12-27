package com.pizza.controller;

import com.pizza.model.dto.PizzaDto;
import com.pizza.model.dto.PizzaDtoWithIngredients;
import com.pizza.model.dto.PizzaIngredientList;
import com.pizza.model.dto.PizzaIngredientWithQuantityList;
import com.pizza.service.PizzaIngredientService;
import com.pizza.service.PizzaService;
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
    public List<PizzaDto> getAllPizzas() {
        return pizzaService.getAllPizzas();
    }

    @GetMapping("/ingredients")
    public List<PizzaDtoWithIngredients> getAllPizzasWithIngredients() {
        return pizzaService.getAllPizzasWithIngredients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDto> getPizzaById(@PathVariable(value = "id") Long id) throws Exception {
        return pizzaService.getPizzaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("{id}/ingredients")
    public ResponseEntity<PizzaDtoWithIngredients> getPizzaWithIngredientsById(@PathVariable(value = "id") Long id) throws Exception {
        return pizzaService.getPizzaWithIngredientsById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<PizzaDto> createPizza(@RequestBody @Valid final PizzaDto pizzaDtoRequest) {
        PizzaDto pizzaDtoResponse = pizzaService.savePizza(pizzaDtoRequest);
        return ResponseEntity.ok(pizzaDtoResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDto> updatePizza(@PathVariable(value = "id") Long id, @RequestBody @Valid PizzaDto newPizzaDto) {
        return pizzaService.updatePizza(id, newPizzaDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable(value = "id") Long id) {
        boolean deleted = pizzaService.deletePizzaById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updateIngredients")
    public ResponseEntity<PizzaIngredientWithQuantityList> updatePizzaIngredients(
            @RequestBody @Valid PizzaIngredientWithQuantityList pizzaIngredientWithQuantityList) {
        PizzaIngredientWithQuantityList returnedPizzaIngredientsList = pizzaIngredientService.updatePizzaIngredients(pizzaIngredientWithQuantityList);
        return ResponseEntity.ok(returnedPizzaIngredientsList);
    }

    @DeleteMapping("/removeIngredients")
    public ResponseEntity<Void> removeIngredientsFromPizza(
            @RequestBody PizzaIngredientList pizzaIngredientList) {
        pizzaIngredientService.removePizzaIngredients(pizzaIngredientList);
        return ResponseEntity.noContent().build();
    }

}
