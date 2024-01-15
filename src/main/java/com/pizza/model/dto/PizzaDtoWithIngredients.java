package com.pizza.model.dto;

import com.pizza.model.Pizza;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class PizzaDtoWithIngredients {

    @Schema(description = "Pizza id", example = "2")
    private Long id;

    @NotEmpty
    @Schema(description = "Name of the pizza", example = "Hawaiian")
    private String name;

    @NotEmpty
    @Schema(description = "List of allergens", example = "Pineapple, Ham")
    private String allergens;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Price of the pizza", example = "11.99")
    private float price;

    @NotEmpty
    @Schema(description = "Type of blat", example = "Thick")
    private String blatType;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Quantity of blat", example = "2")
    private Integer blatQuantity;

    @NotEmpty
    @Schema(description = "Name of base", example = "Cheese")
    private String baseName;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Quantity of base", example = "2")
    private Integer baseQuantity;

    @NotNull
    @Schema(description = "List of ingredients with quantity", example = "[{'ingredientId': 1, 'quantity': 2}]")
    private Set<IngredientWithQuantity> ingredients;

    public static PizzaDtoWithIngredients from(Pizza pizza) {
        PizzaDtoWithIngredients pizzaDto = new PizzaDtoWithIngredients();

        pizzaDto.setId(pizza.getId());
        pizzaDto.setName(pizza.getName());
        pizzaDto.setAllergens(getUniqueAllergens(pizza.getAllergens()));
        pizzaDto.setPrice(pizza.getPrice());
        pizzaDto.setBlatType(pizza.getBlatType());
        pizzaDto.setBlatQuantity(pizza.getBlatQuantity());
        pizzaDto.setBaseName(pizza.getBaseName());
        pizzaDto.setBaseQuantity(pizza.getBaseQuantity());
        pizzaDto.setIngredients(pizza.getPizzaIngredientSet().stream().map(IngredientWithQuantity::from).collect(Collectors.toSet()));

        return pizzaDto;
    }

    public static String getUniqueAllergens(String allergens) {
        return Arrays.stream(allergens.split(",\\s*"))
                .filter(allergen -> !allergen.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .collect(Collectors.joining(", "));
    }

}
