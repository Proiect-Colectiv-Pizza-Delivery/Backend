package com.pizza;

import com.pizza.model.Ingredient;
import com.pizza.model.Pizza;
import com.pizza.model.PizzaIngredient;
import com.pizza.model.PizzaIngredientKey;
import com.pizza.model.dto.IngredientDto;
import com.pizza.model.dto.IngredientWithQuantity;
import com.pizza.model.dto.PizzaDto;
import com.pizza.model.dto.PizzaDtoWithIngredients;
import com.pizza.repository.PizzaRepository;
import com.pizza.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @InjectMocks
    private PizzaService pizzaService;

    @Test
    void getAllPizzas_ShouldReturnListOfPizzas() {

        Pizza pizza1 = new Pizza("Pizza1", "lactose", (float)10.99, "thin", 1, "classic", 1, new HashMap<>());
        pizza1.setId(1L);

        Pizza pizza2 = new Pizza("Pizza2", "lactose", (float)12.99, "thick", 1, "whole wheat", 1, new HashMap<>());
        pizza2.setId(2L);


        // Arrange
        when(pizzaRepository.findAll()).thenReturn(Arrays.asList(
                pizza1,
                pizza2
        ));

        // Act
        List<PizzaDto> result = pizzaService.getAllPizzas();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Pizza1", result.get(0).getName());
        assertEquals("Pizza2", result.get(1).getName());
        assertEquals("lactose", result.get(0).getAllergens());
        assertEquals("lactose", result.get(1).getAllergens());
        assertEquals((float)10.99, result.get(0).getPrice());
        assertEquals((float)12.99, result.get(1).getPrice());
        assertEquals("thin", result.get(0).getBlatType());
        assertEquals("thick", result.get(1).getBlatType());
        assertEquals(1, result.get(0).getBlatQuantity());
        assertEquals(1, result.get(1).getBlatQuantity());
        assertEquals("classic", result.get(0).getBaseName());
        assertEquals("whole wheat", result.get(1).getBaseName());
        assertEquals(1, result.get(0).getBaseQuantity());
        assertEquals(1, result.get(1).getBaseQuantity());
    }

    @Test
    void getAllPizzasWithIngredients_ShouldReturnListOfPizzasWithIngredients() {

        Pizza pizza1 = new Pizza("Pizza1", "lactose", (float)10.99, "thin", 1, "classic", 1, new HashMap<>());
        pizza1.setId(1L);

        Pizza pizza2 = new Pizza("Pizza2", "lactose", (float)12.99, "thick", 1, "whole wheat", 1, new HashMap<>());
        pizza2.setId(2L);

        Ingredient mozzarella = new Ingredient("mozzarella",100,"lactose");
        mozzarella.setId(1L);
        Ingredient tomatoSauce = new Ingredient("tomato sauce", 10, "none");
        tomatoSauce.setId(2L);
        Ingredient pepperoni = new Ingredient("pepperoni",50,"none");
        pepperoni.setId(3L);

        PizzaIngredient pizza1Ingredient1 = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(),mozzarella.getId()),pizza1,mozzarella,1);
        PizzaIngredient pizza1Ingredient2 = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(),tomatoSauce.getId()),pizza1, tomatoSauce,1);

        PizzaIngredient pizza2Ingredient1 = new PizzaIngredient(new PizzaIngredientKey(pizza2.getId(), mozzarella.getId()), pizza2, mozzarella, 2);
        PizzaIngredient pizza2Ingredient2 = new PizzaIngredient(new PizzaIngredientKey(pizza2.getId(), tomatoSauce.getId()), pizza2, tomatoSauce, 1);
        PizzaIngredient pizza2Ingredient3 = new PizzaIngredient(new PizzaIngredientKey(pizza2.getId(), pepperoni.getId()), pizza2, pepperoni, 2);


        Set<PizzaIngredient> pizza1IngredientsSet = new HashSet<>();
        Set<PizzaIngredient> pizza2IngredientsSet = new HashSet<>();

        pizza1IngredientsSet.add(pizza1Ingredient1);
        pizza1IngredientsSet.add(pizza1Ingredient2);

        pizza2IngredientsSet.add(pizza2Ingredient1);
        pizza2IngredientsSet.add(pizza2Ingredient2);
        pizza2IngredientsSet.add(pizza2Ingredient3);

        pizza1.setPizzaIngredientSet(pizza1IngredientsSet);
        pizza2.setPizzaIngredientSet(pizza2IngredientsSet);


        // Arrange
        when(pizzaRepository.findAll()).thenReturn(Arrays.asList(
                pizza1,
                pizza2
        ));

        // Act
        List<PizzaDtoWithIngredients> result = pizzaService.getAllPizzasWithIngredients();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Pizza1", result.get(0).getName());
        assertEquals("Pizza2", result.get(1).getName());

        Set<IngredientWithQuantity> pizza1Ingr = new HashSet<>();
        pizza1Ingr.add(new IngredientWithQuantity(IngredientDto.from(mozzarella), 1));
        pizza1Ingr.add(new IngredientWithQuantity(IngredientDto.from(tomatoSauce), 1));

        assertEquals(pizza1Ingr, result.get(0).getIngredients());

        Set<IngredientWithQuantity> pizza2Ingr = new HashSet<>();
        pizza2Ingr.add(new IngredientWithQuantity(IngredientDto.from(mozzarella), 2));
        pizza2Ingr.add(new IngredientWithQuantity(IngredientDto.from(tomatoSauce), 1));
        pizza2Ingr.add(new IngredientWithQuantity(IngredientDto.from(pepperoni), 2));

        assertEquals(pizza2Ingr, result.get(1).getIngredients());


    }

    @Test
    void getPizzaById_ExistingId_ShouldReturnPizzaDto() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(new Pizza("Pizza1", "Cheese", (float)10.99, "Thin", 1, "Classic", 1, new HashMap<>())));

        // Act
        Optional<PizzaDto> result = pizzaService.getPizzaById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Pizza1", result.get().getName());
        assertEquals((float)10.99, result.get().getPrice());
    }

    @Test
    void getPizzaById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<PizzaDto> result = pizzaService.getPizzaById(1L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void savePizza_ShouldReturnSavedPizzaDto() {

        Pizza newPizza = new Pizza("NewPizza", "lactose", (float)15.99, "thin", 1, "classic", 1, new HashMap<>());

        // Arrange
        PizzaDto inputDto = PizzaDto.from(newPizza);
        when(pizzaRepository.save(any(Pizza.class))).thenAnswer(invocation -> {
            Pizza savedPizza = invocation.getArgument(0);
            savedPizza.setId(1L); // Simulate the save operation
            return savedPizza;
        });

        // Act
        PizzaDto result = pizzaService.savePizza(inputDto);

        // Assert
        assertNotNull(result.getId());
        assertEquals("NewPizza", result.getName());
        assertEquals((float)15.99, result.getPrice());
    }

    @Test
    void deletePizzaById_ExistingId_ShouldReturnTrue() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(new Pizza("Pizza1", "Cheese", (float)10.99, "Thin", 1, "Classic", 1, new HashMap<>())));

        // Act
        boolean result = pizzaService.deletePizzaById(1L);

        // Assert
        assertTrue(result);
        verify(pizzaRepository, times(1)).delete(any(Pizza.class));
    }

    @Test
    void deletePizzaById_NonExistingId_ShouldReturnFalse() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = pizzaService.deletePizzaById(1L);

        // Assert
        assertFalse(result);
        verify(pizzaRepository, never()).delete(any(Pizza.class));
    }

    @Test
    void updatePizza_ExistingId_ShouldReturnUpdatedPizzaDto() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(new Pizza("Pizza1", "Cheese", (float)10.99, "Thin", 1, "Classic", 1, new HashMap<>())));

        Pizza updatedPizza = new Pizza("UpdatedPizza", "lactose", (float)12.99, "thick", 2, "whole wheat", 1, new HashMap<>());
        PizzaDto updatedDto = PizzaDto.from(updatedPizza);

        // Act
        Optional<PizzaDto> result = pizzaService.updatePizza(1L, updatedDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("UpdatedPizza", result.get().getName());
        assertEquals("lactose", result.get().getAllergens());
        assertEquals("thick", result.get().getBlatType());
        assertEquals((float)12.99, result.get().getPrice());
    }

    @Test
    void updatePizza_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        Pizza updatedPizza = new Pizza("UpdatedPizza", "lactose", (float)12.99, "thick", 2, "whole wheat", 1, new HashMap<>());
        PizzaDto updatedDto = PizzaDto.from(updatedPizza);

        // Act
        Optional<PizzaDto> result = pizzaService.updatePizza(1L, updatedDto);

        // Assert
        assertTrue(result.isEmpty());
    }
}

