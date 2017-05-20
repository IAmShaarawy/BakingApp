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

import net.elshaarawy.bakingapp.R;

/**
 * Created by elshaarawy on 20-May-17.
 */

public class VideoPlayerFragment extends Fragment {

    private static final String EXTRA_URI = "extra_uri";
    private Uri mVideoUri;


    private SimpleExoPlayerView mVideoPlayerView;
    private SimpleExoPlayer mVideoPlayer;
    private boolean mReady;
    private int mCurrentFrame;
    private long mBackPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoUri = Uri.parse(getArguments().getString(EXTRA_URI));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player,container,false);
        mVideoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.step_video_player);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mVideoPlayer == null)) {
            initPlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initPlayer(){
        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mVideoPlayerView.setPlayer(mVideoPlayer);


        mVideoPlayer.setPlayWhenReady(mReady);
        mVideoPlayer.seekTo(mCurrentFrame, mBackPosition);

        MediaSource mediaSource = buildMediaSource(mVideoUri);
        mVideoPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mVideoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (mVideoPlayer != null) {
            mBackPosition = mVideoPlayer.getCurrentPosition();
            mCurrentFrame = mVideoPlayer.getCurrentWindowIndex();
            mReady = mVideoPlayer.getPlayWhenReady();
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    public static void attachMe(FragmentManager fragmentManager,int layout,String uri){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URI,uri);
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        videoPlayerFragment.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .replace(layout,videoPlayerFragment)
                .commit();
    }
}
