package com.pizza.model.dto;

import com.pizza.model.PizzaIngredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientIdWithQuantity {

    @NotNull
    @Schema(description = "Ingredient id", example = "1")
    private Long ingredientId;

    @NotNull
    @Schema(description = "Quantity of the ingredient", example = "2")
    private Integer quantity;

    public static IngredientIdWithQuantity from(PizzaIngredient pizzaIngredient) {
        return new IngredientIdWithQuantity(pizzaIngredient.getIngredient().getId(), pizzaIngredient.getQuantity());
    }
}
