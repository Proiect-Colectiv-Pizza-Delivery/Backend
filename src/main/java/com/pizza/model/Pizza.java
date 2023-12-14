package com.pizza.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pizzas")
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String allergens;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private String blatType;

    @Column(nullable = false)
    private Integer blatQuantity;

    @Column(nullable = false)
    private String baseName;

    @Column(nullable = false)
    private Integer baseQuantity;


    @Column(name = "ingredients")
    private String ingredients;

    public Pizza() {
    }

    public Pizza(String name, String allergens, float price, String blatType, Integer blatQuantity, String baseName, Integer baseQuantity, String ingredients) {
        this.name = name;
        this.allergens = allergens;
        this.price = price;
        this.blatType = blatType;
        this.blatQuantity = blatQuantity;
        this.baseName = baseName;
        this.baseQuantity = baseQuantity;
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBlatType() {
        return blatType;
    }

    public void setBlatType(String blatType) {
        this.blatType = blatType;
    }

    public Integer getBlatQuantity() {
        return blatQuantity;
    }

    public void setBlatQuantity(Integer blatQuantity) {
        this.blatQuantity = blatQuantity;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public Integer getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(Integer baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", allergens='" + allergens + '\'' +
                ", baseName='" + baseName + '\'' +
                ", baseQuantity=" + baseQuantity +
                ", blatType='" + blatType + '\'' +
                ", blatQuantity=" + blatQuantity +
                ", ingredients=" + ingredients +
                '}';
    }
}
