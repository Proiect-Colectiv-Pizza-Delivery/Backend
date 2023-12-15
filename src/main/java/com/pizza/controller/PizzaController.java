package com.pizza.controller;

import com.pizza.model.Pizza;
import com.pizza.service.PizzaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@OpenAPIDefinition(
        security = {@SecurityRequirement(name = "bearerAuth")}
)
public class PizzaController {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public ResponseEntity<List<Pizza>> getAllPizzas() {
        try {
            return ResponseEntity.ok(pizzaService.getAllPizzas());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Pizza> getPizzaById(@PathVariable Long id) {
        return pizzaService.getPizzaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pizza> createPizza(@RequestBody Pizza pizza) {
        Pizza savedPizza = pizzaService.savePizza(pizza);
        return ResponseEntity.ok(savedPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pizza> updatePizza(@PathVariable Long id, @RequestBody Pizza pizza) {
        if (pizzaService.getPizzaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pizza.setId(id);
        Pizza updatedPizza = pizzaService.savePizza(pizza);
        return ResponseEntity.ok(updatedPizza);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePizza(@PathVariable Long id) {
        if (pizzaService.getPizzaById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        pizzaService.deletePizza(id);
        return ResponseEntity.ok("Delete successfully id = " + id);
    }
}
