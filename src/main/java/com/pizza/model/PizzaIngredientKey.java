package com.pizza.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PizzaIngredientKey implements Serializable {

    private Long pizzaId;
    private Long ingredientId;

}
