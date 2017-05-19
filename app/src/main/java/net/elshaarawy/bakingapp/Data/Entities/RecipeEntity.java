package net.elshaarawy.bakingapp.Data.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class RecipeEntity implements Parcelable {

    private String id,name;
    private List<IngredientEntity> ingredients;
    private List<StepEntity> steps;
    private String servings,image;

    public RecipeEntity(String id, String name, List<IngredientEntity> ingredients, List<StepEntity> steps, String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    protected RecipeEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = in.createTypedArrayList(IngredientEntity.CREATOR);
        steps = in.createTypedArrayList(StepEntity.CREATOR);
        servings = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeString(servings);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeEntity> CREATOR = new Creator<RecipeEntity>() {
        @Override
        public RecipeEntity createFromParcel(Parcel in) {
            return new RecipeEntity(in);
        }

        @Override
        public RecipeEntity[] newArray(int size) {
            return new RecipeEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepEntity> getSteps() {
        return steps;
    }

    public void setSteps(List<StepEntity> steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
