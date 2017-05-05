package me.josephcosentino.model;

import java.io.Serializable;

/**
 * @author Joseph Cosentino.
 */
public class MealIngredientId implements Serializable {

    private long meal;
    private long ingredient;

    public long getMeal() {
        return meal;
    }

    public void setMeal(long meal) {
        this.meal = meal;
    }

    public long getIngredient() {
        return ingredient;
    }

    public void setIngredient(long ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MealIngredientId that = (MealIngredientId) o;

        if (meal != that.meal) return false;
        return ingredient == that.ingredient;
    }

    @Override
    public int hashCode() {
        int result = (int) (meal ^ (meal >>> 32));
        result = 31 * result + (int) (ingredient ^ (ingredient >>> 32));
        return result;
    }
}
