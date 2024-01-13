package com.pizza.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PizzaIngredientWithQuantityList {

    @NotNull
    @Schema(description = "Pizza id", example = "1")
    private Long pizzaId;

    @NotNull
    @Schema(description = "List of ingredients with their quantities", example = "[{'ingredient': {'id': 1, 'name': 'Peanut Butter', 'stock': 100, 'allergens': 'Nuts'}, 'quantity': 2}]")
    private List<IngredientWithQuantity> ingredientsList;
}
