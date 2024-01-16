package com.pizza;

import com.pizza.model.Ingredient;
import com.pizza.model.dto.IngredientDto;
import com.pizza.repository.IngredientRepository;
import com.pizza.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void getAllIngredients_ShouldReturnListOfIngredients() {
        Ingredient ingr1 = new Ingredient("Ingredient1", 10, "Allergen1");
        ingr1.setId(1L);

        Ingredient ingr2 =  new Ingredient("Ingredient2", 20, "Allergen2");
        ingr2.setId(2L);

        // Arrange
        when(ingredientRepository.findAll()).thenReturn(Arrays.asList(
                ingr1,
                ingr2
        ));

        // Act
        List<IngredientDto> result = ingredientService.getAllIngredients();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L,result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Ingredient1", result.get(0).getName());
        assertEquals("Ingredient2", result.get(1).getName());
    }

    @Test
    void getIngredientById_ExistingId_ShouldReturnIngredientDto() {

        Ingredient ingr1 = new Ingredient("Ingredient1", 10, "Allergen1");
        ingr1.setId(1L);

        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingr1));

        // Act
        Optional<IngredientDto> result = ingredientService.getIngredientById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Ingredient1", result.get().getName());
        assertEquals(10, result.get().getStock());
    }

    @Test
    void getIngredientById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<IngredientDto> result = ingredientService.getIngredientById(1L);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void saveIngredient_ShouldReturnSavedIngredientDto() {
        Ingredient inputIngredient = new Ingredient("NewIngredient", 30, "NewAllergen");
        inputIngredient.setId(1L);

        // Arrange
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(inputIngredient);

        // Act
        IngredientDto result = ingredientService.saveIngredient(new IngredientDto());

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("NewIngredient", result.getName());
        assertEquals(30, result.getStock());
        assertEquals("NewAllergen", result.getAllergens());
    }

    @Test
    void deleteIngredientById_ExistingId_ShouldReturnTrue() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(new Ingredient("Ingredient1", 10, "Allergen1")));

        // Act
        boolean result = ingredientService.deleteIngredientById(1L);

        // Assert
        assertTrue(result);
        verify(ingredientRepository, times(1)).delete(any(Ingredient.class));
    }

    @Test
    void deleteIngredientById_NonExistingId_ShouldReturnFalse() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = ingredientService.deleteIngredientById(1L);

        // Assert
        assertFalse(result);
        verify(ingredientRepository, never()).delete(any(Ingredient.class));
    }

    @Test
    void updateIngredient_ExistingId_ShouldReturnUpdatedIngredientDto() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(new Ingredient( "Ingredient1", 10, "Allergen1")));

        Ingredient updatedIngredient = new Ingredient("UpdatedIngredient", 20, "UpdatedAllergen");
        IngredientDto updatedDto = IngredientDto.from(updatedIngredient);

        // Act
        Optional<IngredientDto> result = ingredientService.updateIngredient(1L, updatedDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("UpdatedIngredient", result.get().getName());
        assertEquals(20, result.get().getStock());
        assertEquals("UpdatedAllergen", result.get().getAllergens());
    }


    @Test
    void updateIngredient_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(ingredientRepository.findById(1L)).thenReturn(Optional.empty());

        Ingredient updatedIngredient = new Ingredient("UpdatedIngredient", 20, "UpdatedAllergen");
        IngredientDto updatedDto = IngredientDto.from(updatedIngredient);

        // Act
        Optional<IngredientDto> result = ingredientService.updateIngredient(1L, updatedDto);

        // Assert
        assertTrue(result.isEmpty());
    }
}

