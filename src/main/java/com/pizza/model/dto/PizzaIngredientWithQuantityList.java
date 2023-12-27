package com.pizza.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PizzaIngredientWithQuantityList {

    @NotNull
    private Long pizzaId;
    @NotNull
    private List<IngredientWithQuantity> ingredientsList;
}
