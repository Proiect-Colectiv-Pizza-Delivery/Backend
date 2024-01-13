package com.pizza.model.dto;

import com.pizza.model.PizzaIngredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientWithQuantity {

    @NotNull
    @Schema(description = "Ingredient", example = "{'id': 1, 'name': 'Peanut Butter', 'stock': 100, 'allergens': 'Nuts'}")
    private IngredientDto ingredient;

    @NotNull
    @Schema(description = "Quantity of the ingredient", example = "2")
    private Integer quantity;

    public static IngredientWithQuantity from(PizzaIngredient pizzaIngredient) {
        return new IngredientWithQuantity(IngredientDto.from(pizzaIngredient.getIngredient()), pizzaIngredient.getQuantity());
    }

}
