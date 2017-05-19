package net.elshaarawy.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.R;

import java.util.List;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {

    List<IngredientEntity> mIngredientEntities;

    public IngredientsAdapter(List<IngredientEntity> mIngredientEntities) {
        this.mIngredientEntities = mIngredientEntities;
    }

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_ingredients,parent,false);
        return new IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {
        IngredientEntity ingredientEntity = mIngredientEntities.get(position);

        String data = ingredientEntity.getQuantity()+" "
                +ingredientEntity.getMeasure()+" -> "
                +ingredientEntity.getIngredient();

        holder.bindData(data);

    }

    @Override
    public int getItemCount() {
        return mIngredientEntities.size();
    }

    class IngredientsHolder extends RecyclerView.ViewHolder{

        TextView dataTextView ;

        public IngredientsHolder(View itemView) {
            super(itemView);
            dataTextView = (TextView) itemView;
        }
        void bindData(String data){
            dataTextView.setText(data);
        }
    }
}
