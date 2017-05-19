package net.elshaarawy.bakingapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class StepFragment extends Fragment {

    private static final String EXTRA_RECIPE = "extra_recipe";
    StepEntity mStepEntity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStepEntity = getArguments().getParcelable(EXTRA_RECIPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        TextView textView = (TextView) view.findViewById(R.id.url);
        textView.setText(mStepEntity.getDescription());
        return view;
    }

    public static void attachMe(FragmentManager fragmentManager,int layout,StepEntity stepEntity){
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE,stepEntity);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(layout, stepFragment)
                .commit();
    }
}
