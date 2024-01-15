package com.pizza.model.dto;

import com.pizza.model.Ingredient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class IngredientDto {

    @Schema(description = "Ingredient id", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "Name of the ingredient", example = "Tomato")
    private String name;

    @NotNull
    @Schema(description = "Stock available for the ingredient", example = "100")
    private Integer stock;

    @NotEmpty
    @Schema(description = "String of allergens present in the ingredient", example = "Nuts, Dairy")
    private String allergens;

    public static IngredientDto from(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setStock(ingredient.getStock());
        ingredientDto.setAllergens(ingredient.getAllergens());

        return ingredientDto;

    }


}
