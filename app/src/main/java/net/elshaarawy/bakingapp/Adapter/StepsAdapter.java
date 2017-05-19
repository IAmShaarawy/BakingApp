package net.elshaarawy.bakingapp.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.elshaarawy.bakingapp.Data.Entities.IngredientEntity;
import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;

import java.util.List;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {
    List<StepEntity> mStepEntities;

    public StepsAdapter(List<StepEntity> mStepEntities) {
        this.mStepEntities = mStepEntities;
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_step,parent,false);
        return new StepsHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsHolder holder, int position) {
        StepEntity stepEntity = mStepEntities.get(position);

        String data = stepEntity.getShortDescription();

        holder.bindData(data);

    }

    @Override
    public int getItemCount() {
        return mStepEntities.size();
    }

    class StepsHolder extends RecyclerView.ViewHolder{

        TextView dataTextView ;

        public StepsHolder(View itemView) {
            super(itemView);
            dataTextView = (TextView) itemView;
        }
        void bindData(String data){
            dataTextView.setText(data);
        }
    }
}
