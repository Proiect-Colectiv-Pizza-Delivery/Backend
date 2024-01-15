package com.pizza;

import com.pizza.exception.CustomException;
import com.pizza.model.Ingredient;
import com.pizza.model.Pizza;
import com.pizza.model.PizzaIngredient;
import com.pizza.model.PizzaIngredientKey;
import com.pizza.model.dto.*;
import com.pizza.repository.IngredientRepository;
import com.pizza.repository.PizzaIngredientRepository;
import com.pizza.repository.PizzaRepository;
import com.pizza.service.PizzaIngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaIngredientServiceTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private PizzaIngredientRepository pizzaIngredientRepository;

    @InjectMocks
    private PizzaIngredientService pizzaIngredientService;

    @Test
    void updatePizzaIngredients_ShouldUpdateIngredientsAndReturnList() throws CustomException {
        // Arrange
        Pizza pizza1 = new Pizza("Pizza1", "lactose", (float)10.99, "thin", 1, "classic", 1, new HashMap<>());
        pizza1.setId(1L);
        pizza1.setPizzaIngredientSet(new HashSet<>());

        Ingredient mozzarella = new Ingredient("mozzarella",100,"lactose");
        mozzarella.setId(1L);

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(mozzarella));

        PizzaIngredient pizza1Ingredient1 = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(),mozzarella.getId()),pizza1,mozzarella,1);

        pizza1.getPizzaIngredientSet().add(pizza1Ingredient1);

        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza1));


        IngredientIdWithQuantity ingredient1  = new IngredientIdWithQuantity(mozzarella.getId(),2);
        List<IngredientIdWithQuantity> ingredientsList = Arrays.asList(ingredient1);

        PizzaIngredientIdsWithQuantityList inputList = new PizzaIngredientIdsWithQuantityList();
        inputList.setPizzaId(pizza1.getId());
        inputList.setIngredientsIdList(ingredientsList);


        when(pizzaIngredientRepository.findById(any())).thenReturn(Optional.empty());
        when(pizzaIngredientRepository.save(any(PizzaIngredient.class))).thenReturn(new PizzaIngredient(pizza1Ingredient1.getId(),pizza1,mozzarella,2));
        when(pizzaIngredientRepository.findById(any(PizzaIngredientKey.class))).thenReturn(Optional.of(pizza1Ingredient1));

        // Act
        PizzaIngredientIdsWithQuantityList result = pizzaIngredientService.updatePizzaIngredients(inputList);

        // Assert
        assertNotNull(result);
        assertEquals(pizza1.getId(), result.getPizzaId());
        assertEquals(1, result.getIngredientsIdList().size());
        assertEquals(1, result.getIngredientsIdList().get(0).getIngredientId());
        assertEquals(3, result.getIngredientsIdList().get(0).getQuantity());
        verify(pizzaIngredientRepository, times(1)).save(any(PizzaIngredient.class));
    }

    @Test
    void updateIngredientInPizza_ShouldUpdateIngredientAndReturnDto() throws CustomException {
        // Arrange
        Integer quantity = 2;
        Long pizzaId = 1L;
        Long ingredientId = 2L;

        Pizza pizza = new Pizza();
        Ingredient ingredient = new Ingredient();
        PizzaIngredient pizzaIngredient = new PizzaIngredient(new PizzaIngredientKey(pizzaId, ingredientId), pizza, ingredient, 1);

        when(pizzaRepository.findById(pizzaId)).thenReturn(Optional.of(pizza));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));
        when(pizzaIngredientRepository.findById(any())).thenReturn(Optional.of(pizzaIngredient));
        when(pizzaIngredientRepository.save(any())).thenReturn(pizzaIngredient);

        // Act
        PizzaIngredientDto result = pizzaIngredientService.updateIngredientInPizza(quantity, pizzaId, ingredientId);

        // Assert
        assertNotNull(result);
        assertEquals(pizzaId, result.getPizzaId());
        assertEquals(ingredientId, result.getIngredientId());
        assertEquals(3, pizzaIngredient.getQuantity());
    }

    @Test
    void removePizzaIngredientsAndReturn_ShouldRemoveIngredientsAndReturnList() throws CustomException {
        // Arrange
        Pizza pizza1 = new Pizza("Pizza1", "lactose", (float)10.99, "thin", 1, "classic", 1, new HashMap<>());
        pizza1.setId(1L);

        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza1));


        Ingredient mozzarella = new Ingredient("mozzarella",100,"lactose");
        mozzarella.setId(1L);
        Ingredient tomatoSauce = new Ingredient("tomato sauce", 10, "none");
        tomatoSauce.setId(2L);
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(mozzarella));
        when(ingredientRepository.findById(2L)).thenReturn(Optional.of(tomatoSauce));

        List<Long> ingredientsList = Arrays.asList(1L, 2L);

        PizzaIngredient pizza1Ingredient1 = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(),mozzarella.getId()),pizza1, mozzarella, 1);
        PizzaIngredient pizza1Ingredient2 = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(),tomatoSauce.getId()),pizza1, tomatoSauce, 1);

        when(pizzaIngredientRepository.findByPizzaIdAndIngredientId(pizza1.getId(), mozzarella.getId())).thenReturn(Optional.of(pizza1Ingredient1));
        when(pizzaIngredientRepository.findByPizzaIdAndIngredientId(pizza1.getId(), tomatoSauce.getId())).thenReturn(Optional.of(pizza1Ingredient2));

        Set<PizzaIngredient> pizza1IngredientsSet = new HashSet<>();
        pizza1IngredientsSet.add(pizza1Ingredient1);
        pizza1IngredientsSet.add(pizza1Ingredient2);
        pizza1.setPizzaIngredientSet(pizza1IngredientsSet);


        PizzaIngredientList pizzaIngredientList = new PizzaIngredientList();
        pizzaIngredientList.setPizzaId(pizza1.getId());
        pizzaIngredientList.setIngredientsList(ingredientsList);

        // Act
        PizzaIngredientWithQuantityList result = pizzaIngredientService.removePizzaIngredientsAndReturn(pizzaIngredientList);

        // Assert
        assertNotNull(result);
        assertEquals(pizza1.getId(), result.getPizzaId());
        verify(pizzaIngredientRepository, times(2)).delete(any(PizzaIngredient.class));
    }

    @Test
    void removeIngredientFromPizza_ShouldRemoveIngredient() throws CustomException {
        // Arrange
        Pizza pizza1 = new Pizza("Pizza1", "lactose", (float)10.99, "thin", 1, "classic", 1, new HashMap<>());
        pizza1.setId(1L);
        pizza1.setPizzaIngredientSet(new HashSet<>());

        Ingredient tomatoSauce = new Ingredient("tomato sauce", 10, "none");
        tomatoSauce.setId(2L);

        PizzaIngredient pizzaIngredient = new PizzaIngredient(new PizzaIngredientKey(pizza1.getId(), tomatoSauce.getId()), pizza1, tomatoSauce, 1);

        when(pizzaRepository.findById(pizza1.getId())).thenReturn(Optional.of(pizza1));
        when(ingredientRepository.findById(tomatoSauce.getId())).thenReturn(Optional.of(tomatoSauce));
        when(pizzaIngredientRepository.findByPizzaIdAndIngredientId(pizza1.getId(), tomatoSauce.getId())).thenReturn(Optional.of(pizzaIngredient));

        Set<PizzaIngredient> pizza1IngredientsSet = new HashSet<>();
        pizza1IngredientsSet.add(pizzaIngredient);
        pizza1.setPizzaIngredientSet(pizza1IngredientsSet);

        // Act
        pizzaIngredientService.removeIngredientFromPizza(pizza1.getId(), tomatoSauce.getId());

        // Assert
        assertEquals("lactose", pizza1.getAllergens());
        verify(pizzaIngredientRepository, times(1)).delete(any(PizzaIngredient.class));
    }

}

