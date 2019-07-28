package org.udacity.bakyflacky.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.udacity.bakyflacky.R;
import org.udacity.bakyflacky.recipe.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    @BindView(R.id.tv_step_short_description) TextView description;
    @BindView(R.id.btn_step_instruction) Button instructions;
    @BindView(R.id.img_step_thumbnail) ImageView thumbnail;
    @BindView(R.id.player_view) SimpleExoPlayerView playerView;

    private static final String STEP_OBJECT = "StepObject";

    private Step step;
    private SimpleExoPlayer player;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STEP_OBJECT, step);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(STEP_OBJECT);
        }

        View rootView = inflater.inflate(R.layout.fragment_steps_details, container, false);
        ButterKnife.bind(this, rootView);

        updateView();
        initializePlayer();

        return rootView;
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    public void setStep(Step step) {
        this.step= step;
    }

    private void updateView() {
        this.description.setText(step.shortDescription);
        this.instructions.setText(step.description);
    }

    private void initializePlayer() {
        if (step != null && step.videoURL != null &&  !step.videoURL.isEmpty()) {
            Context context = getContext();
            player = ExoPlayerFactory.newSimpleInstance(getContext());
            playerView.setPlayer(player);
            playerView.setVisibility(View.VISIBLE);

            Uri videoUri = Uri.parse(step.videoURL);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, getString(R.string.app_name)));
            MediaSource videoSource = new ExtractorMediaSource(
                    videoUri,
                    dataSourceFactory,
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            player.prepare(videoSource);
        }
    }

    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }
}
