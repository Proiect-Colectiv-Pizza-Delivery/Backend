package com.pizza.model.dto;

import com.pizza.model.Pizza;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PizzaDto {

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

    public static PizzaDto from(Pizza pizza) {
        PizzaDto pizzaDto = new PizzaDto();

        pizzaDto.setId(pizza.getId());
        pizzaDto.setName(pizza.getName());
        pizzaDto.setAllergens(pizza.getAllergens());
        pizzaDto.setPrice(pizza.getPrice());
        pizzaDto.setBlatType(pizza.getBlatType());
        pizzaDto.setBlatQuantity(pizza.getBlatQuantity());
        pizzaDto.setBaseName(pizza.getBaseName());
        pizzaDto.setBaseQuantity(pizza.getBaseQuantity());

        return pizzaDto;
    }

}
