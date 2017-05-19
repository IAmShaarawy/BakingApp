package net.elshaarawy.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.Fragments.StepFragment;
import net.elshaarawy.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity {

    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_ID = "extra_id";
    private List<StepEntity> mStepEntities;
    private StepEntity mStepEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        mStepEntities = intent.getParcelableArrayListExtra(EXTRA_STEPS);
        mStepEntity = mStepEntities.get(intent.getIntExtra(EXTRA_ID,0));

        getSupportActionBar().setTitle(mStepEntity.getShortDescription());
        if (findViewById(R.id.step_detail_portrait)!= null){
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.step_detail_portrait,mStepEntity);
        }

    }

    public static void startMe(Context context, List<StepEntity> stepEntities,int itemIndex){
        Intent intent = new Intent(context,StepActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_STEPS,(ArrayList) stepEntities);
        intent.putExtra(EXTRA_ID,itemIndex);
        context.startActivity(intent);

    }
}
