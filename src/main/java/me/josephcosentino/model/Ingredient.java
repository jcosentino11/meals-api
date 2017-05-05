package me.josephcosentino.model;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Joseph Cosentino.
 */
@Entity(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer calories;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @OneToMany(mappedBy = "ingredient")
    private Set<MealIngredient> mealsIngredients;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public Set<MealIngredient> getMealsIngredients() {
        return mealsIngredients;
    }

    public void setMealsIngredients(Set<MealIngredient> mealsIngredients) {
        this.mealsIngredients = mealsIngredients;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", foodType=" + foodType +
                ", mealsIngredients=" + mealsIngredients +
                '}';
    }
}
