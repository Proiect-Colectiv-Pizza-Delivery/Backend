package com.pizza.model.dto;

import com.pizza.model.Pizza;
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
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String allergens;

    @NotNull
    @Min(value=1)
    private float price;

    @NotEmpty
    private String blatType;

    @NotNull
    @Min(value=1)
    private Integer blatQuantity;

    @NotEmpty
    private String baseName;

    @NotNull
    @Min(value=1)
    private Integer baseQuantity;
    @NotNull
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
