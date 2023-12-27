package com.pizza.model.dto;

import com.pizza.model.PizzaIngredient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientWithQuantity {

    @NotNull
    private Long ingredientId;

    @NotNull
    private Integer quantity;

    public static IngredientWithQuantity from(PizzaIngredient pizzaIngredient) {
        return new IngredientWithQuantity(pizzaIngredient.getId().getIngredientId(), pizzaIngredient.getQuantity());
    }

}
