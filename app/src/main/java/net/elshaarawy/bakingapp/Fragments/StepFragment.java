package net.elshaarawy.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.R;

import static android.view.View.GONE;

/**
 * Created by elshaarawy on 19-May-17.
 */

public class StepFragment extends Fragment {

    private static final String EXTRA_RECIPE = "extra_recipe";
    private StepEntity mStepEntity;

    private TextView mStepDescTextView;

    private ImageView stepImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStepEntity = getArguments().getParcelable(EXTRA_RECIPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        stepImageView = (ImageView) view.findViewById(R.id.step_img);

        if (mStepEntity.getVideoURL().equals("")) {
            view.findViewById(R.id.step_video_container).setVisibility(View.GONE);

            stepImageView.setVisibility(View.VISIBLE);
            if (!mStepEntity.getThumbnailURL().equals(""))
                Picasso.with(getContext())
                        .load(mStepEntity.getThumbnailURL())
                        .error(R.drawable.error)
                        .placeholder(R.drawable.placeholder)
                        .into(stepImageView);
        }

        if (savedInstanceState == null) {
            VideoPlayerFragment.attachMe(getFragmentManager(), R.id.step_video_container, mStepEntity.getVideoURL());
        }

        mStepDescTextView = (TextView) view.findViewById(R.id.step_desc);
        mStepDescTextView.setText(mStepEntity.getDescription());

        return view;
    }

    public static void attachMe(FragmentManager fragmentManager, int layout, StepEntity stepEntity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RECIPE, stepEntity);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);

        fragmentManager
                .beginTransaction()
                .replace(layout, stepFragment)
                .commit();
    }
}
