package com.pizza.repository;

import com.pizza.model.PizzaIngredient;
import com.pizza.model.PizzaIngredientKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PizzaIngredientRepository extends JpaRepository<PizzaIngredient, PizzaIngredientKey> {
    Optional<PizzaIngredient> findByPizzaIdAndIngredientId(Long pizzaId, Long ingredientId);

}
