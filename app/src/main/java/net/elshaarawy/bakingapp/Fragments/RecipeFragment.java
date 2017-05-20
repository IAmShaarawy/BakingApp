package net.elshaarawy.bakingapp.Fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.elshaarawy.bakingapp.Adapter.IngredientsAdapter;
import net.elshaarawy.bakingapp.Adapter.StepsAdapter;
import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class RecipeFragment extends Fragment implements StepsAdapter.StepItemClickListener {

    private static final String EXTRA_INGREDIENTS = "extra_ingredients";
    private static final String EXTRA_STEPS = "extra_steps";
    private static final String EXTRA_LISTENER = "extra_listener";
    private static final String EXTRA_HAS_INDICATOR = "extra_has_indicator";
    private List<IngredientEntity> mIngredientEntities;
    private List<StepEntity> mStepEntities;
    private RecyclerView mIngredientsRv, mStepsRv;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    private RecipeFragmentCallbacks mRecipeFragmentCallbacks;
    private boolean hasIndicator;

    public void setmRecipeFragmentCallbacks(RecipeFragmentCallbacks mRecipeFragmentCallbacks) {
        this.mRecipeFragmentCallbacks = mRecipeFragmentCallbacks;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredientEntities = getArguments().getParcelableArrayList(EXTRA_INGREDIENTS);
        mStepEntities = getArguments().getParcelableArrayList(EXTRA_STEPS);
        hasIndicator = getArguments().getBoolean(EXTRA_HAS_INDICATOR);
        if (savedInstanceState!=null){
            mRecipeFragmentCallbacks = savedInstanceState.getParcelable(EXTRA_LISTENER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        mIngredientsRv = (RecyclerView) view.findViewById(R.id.recipe_ingredients_RV);
        mIngredientsRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mIngredientsRv.setLayoutManager(linearLayoutManager);
        mIngredientsAdapter = new IngredientsAdapter(mIngredientEntities);
        mIngredientsRv.setAdapter(mIngredientsAdapter);

        mStepsRv = (RecyclerView) view.findViewById(R.id.recipe_steps_RV);
        mStepsRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        mStepsRv.setLayoutManager(linearLayoutManager1);
        mStepsAdapter = new StepsAdapter(mStepEntities,this);
        mStepsAdapter.setHasIndicator(hasIndicator);
        mStepsRv.setAdapter(mStepsAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int position) {
        mRecipeFragmentCallbacks.recipeFragmentItemClickListener(position);
        mIngredientsRv.scrollToPosition(position);
    }

    public static void attachMe(FragmentManager fragmentManager, int layout_container,
                                List<IngredientEntity> ingredientEntities, List<StepEntity> stepEntities,
                                RecipeFragmentCallbacks callbacks,
                                boolean hasIndicator) {
        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_INGREDIENTS, (ArrayList) ingredientEntities);
        bundle.putParcelableArrayList(EXTRA_STEPS, (ArrayList) stepEntities);
        bundle.putBoolean(EXTRA_HAS_INDICATOR,hasIndicator);
        recipeFragment.setArguments(bundle);
        recipeFragment.setmRecipeFragmentCallbacks(callbacks);
        fragmentManager
                .beginTransaction()
                .replace(layout_container, recipeFragment)
                .commit();
    }

    public interface RecipeFragmentCallbacks {
        void recipeFragmentItemClickListener(int position);
    }
}
