package net.elshaarawy.bakingapp.Data.Entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by elshaarawy on 18-May-17.
 */

public class IngredientEntity implements Parcelable {
    private String quantity,measure,ingredient;

    public IngredientEntity(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }


    protected IngredientEntity(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IngredientEntity> CREATOR = new Creator<IngredientEntity>() {
        @Override
        public IngredientEntity createFromParcel(Parcel in) {
            return new IngredientEntity(in);
        }

        @Override
        public IngredientEntity[] newArray(int size) {
            return new IngredientEntity[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
