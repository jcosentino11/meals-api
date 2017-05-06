package me.josephcosentino.model;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Joseph Cosentino.
 */
@Entity(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer prepTime;

    @Column(nullable = false)
    private Integer cookTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String recipe;

    @OneToMany(mappedBy = "meal")
    private Set<MealIngredient> mealsIngredients;

    @OneToMany(mappedBy = "meal")
    private Set<PlannedMeal> plannedMeals;

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

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public Set<MealIngredient> getMealsIngredients() {
        return mealsIngredients;
    }

    public void setMealsIngredients(Set<MealIngredient> mealsIngredients) {
        this.mealsIngredients = mealsIngredients;
    }

    public Set<PlannedMeal> getPlannedMeals() {
        return plannedMeals;
    }

    public void setPlannedMeals(Set<PlannedMeal> plannedMeals) {
        this.plannedMeals = plannedMeals;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", mealType=" + mealType +
                ", recipe='" + recipe + '\'' +
                ", mealsIngredients=" + mealsIngredients +
                ", plannedMeals=" + plannedMeals +
                '}';
    }
}
