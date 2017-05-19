package net.elshaarawy.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.elshaarawy.bakingapp.Data.Entities.RecipeEntity;
import net.elshaarawy.bakingapp.R;

import java.util.List;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesHolder> {

    List<RecipeEntity> mRecipeEntities;
    RecipeItemClickListener mRecipeItemClickListener;

    public RecipesAdapter(List<RecipeEntity> mRecipeEntities, RecipeItemClickListener mRecipeItemClickListener) {
        this.mRecipeEntities = mRecipeEntities;
        this.mRecipeItemClickListener = mRecipeItemClickListener;
    }

    @Override
    public RecipesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recipe,parent,false);

        return new RecipesHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesHolder holder, int position) {
        RecipeEntity recipeEntity = mRecipeEntities.get(position);

        holder.bindData(recipeEntity.getImage(),recipeEntity.getName(),recipeEntity.getServings());
    }

    @Override
    public int getItemCount() {
        return mRecipeEntities.size();
    }

    public void restData(List<RecipeEntity> recipeEntities){
        mRecipeEntities.clear();
        mRecipeEntities.addAll(recipeEntities);
        this.notifyDataSetChanged();
    }

    class RecipesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView recipeImg;
        private TextView recipeHeader,recipeSubHeader;
        Context context;

        public RecipesHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            recipeImg = (ImageView) itemView.findViewById(R.id.recipe_img);
            recipeHeader = (TextView) itemView.findViewById(R.id.recipe_header);
            recipeSubHeader = (TextView) itemView.findViewById(R.id.recipe_subHeader);
            itemView.setOnClickListener(this);
        }

        public void bindData(String imgUri, String header,String subHeader){
            imgUri = imgUri.equals("")?"https://udacity.com":imgUri;
            Picasso.with(context)
                    .load(imgUri)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(recipeImg);
            recipeHeader.setText(header);
            String s = context.getString(R.string.format_servings,subHeader);
            recipeSubHeader.setText(s);
        }

        @Override
        public void onClick(View v) {
            mRecipeItemClickListener.onItemClick(mRecipeEntities.get(getAdapterPosition()));
        }
    }

    public interface RecipeItemClickListener {
        void onItemClick(RecipeEntity recipeEntity);
    }
}
