package me.josephcosentino.model;

import java.io.Serializable;

/**
 * @author Joseph Cosentino.
 */
public class UserMealId implements Serializable {

    private long user;
    private long meal;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getMeal() {
        return meal;
    }

    public void setMeal(long meal) {
        this.meal = meal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMealId that = (UserMealId) o;

        if (user != that.user) return false;
        return meal == that.meal;
    }

    @Override
    public int hashCode() {
        int result = (int) (user ^ (user >>> 32));
        result = 31 * result + (int) (meal ^ (meal >>> 32));
        return result;
    }
}
