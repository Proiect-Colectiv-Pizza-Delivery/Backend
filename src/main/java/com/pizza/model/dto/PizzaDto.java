package com.pizza.model.dto;

import com.pizza.model.Pizza;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Data
public class PizzaDto {

    @Schema(description = "Pizza id", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "Name of the pizza", example = "Margherita")
    private String name;

    @NotEmpty
    @Schema(description = "String of allergens", example = "Tomatoes, Cheese")
    private String allergens;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Price of the pizza", example = "9.99")
    private float price;

    @NotEmpty
    @Schema(description = "Type of blat", example = "Thin")
    private String blatType;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Quantity of blat", example = "1")
    private Integer blatQuantity;

    @NotEmpty
    @Schema(description = "Name of base", example = "Tomato")
    private String baseName;

    @NotNull
    @Min(value = 1)
    @Schema(description = "Quantity of base", example = "1")
    private Integer baseQuantity;

    public static PizzaDto from(Pizza pizza) {
        PizzaDto pizzaDto = new PizzaDto();

        pizzaDto.setId(pizza.getId());
        pizzaDto.setName(pizza.getName());
        pizzaDto.setAllergens(getUniqueAllergens(pizza.getAllergens()));
        pizzaDto.setPrice(pizza.getPrice());
        pizzaDto.setBlatType(pizza.getBlatType());
        pizzaDto.setBlatQuantity(pizza.getBlatQuantity());
        pizzaDto.setBaseName(pizza.getBaseName());
        pizzaDto.setBaseQuantity(pizza.getBaseQuantity());

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
