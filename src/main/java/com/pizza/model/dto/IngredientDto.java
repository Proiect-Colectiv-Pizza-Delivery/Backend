package com.pizza.model.dto;

import com.pizza.model.Ingredient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class IngredientDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Integer stock;

    @NotEmpty
    private String allergens;

    public static IngredientDto from(Ingredient ingredient){
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setStock(ingredient.getStock());
        ingredientDto.setAllergens(ingredient.getAllergens());

        return ingredientDto;

    }


}
