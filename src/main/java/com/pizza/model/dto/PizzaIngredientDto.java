package com.pizza.model.dto;

import com.pizza.model.PizzaIngredient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PizzaIngredientDto {

    @Min(value = 0)
    private Integer quantity;

    @NotNull
    private Long pizzaId;
    @NotNull
    private Long ingredientId;

    public static PizzaIngredientDto from(PizzaIngredient pizzaIngredient, Long pizzaId, Long ingredientId){
        PizzaIngredientDto pizzaIngredientDto = new PizzaIngredientDto();
        pizzaIngredientDto.setQuantity(pizzaIngredient.getQuantity());
        pizzaIngredientDto.setPizzaId(pizzaId);
        pizzaIngredientDto.setIngredientId(ingredientId);
        return pizzaIngredientDto;
    }

}
