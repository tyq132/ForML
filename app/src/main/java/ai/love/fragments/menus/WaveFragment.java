package ai.love.fragments.menus;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ai.love.R;
import ai.love.utils.ClickListenerUtil;
import ai.love.view.WaveView;

/**
 *波浪主题菜单页
 *
 * Created by James tang on 2020/9/1.
 */
public class WaveFragment extends Fragment {
    private ImageView icon1;
    private ImageView ship;
    private ImageView bottle;
    private WaveView waveView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wave, container, false);
        waveView =  view.findViewById(R.id.wave_view);
        icon1 = view.findViewById(R.id.baby);
        ship = view.findViewById(R.id.ship);
        bottle = view.findViewById(R.id.bottle);

        icon1.setOnClickListener(new ClickListenerUtil() {
            @Override
            public void onMultiClick(View v) {
                Log.e("爱神的箭后方可：","");
            }
        });
        initWaveIcon();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initWaveIcon() {
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        final FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.gravity = Gravity.BOTTOM|Gravity.CENTER;
        final FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.gravity = Gravity.BOTTOM|Gravity.RIGHT;
        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {

                lp.setMargins(0,0,0,(int)y+14);
                icon1.setLayoutParams(lp);
                lp1.setMargins(0,0,0,(int)y+4);
                ship.setLayoutParams(lp1);
                lp2.setMargins(0,0,70,(int)y-20);
                bottle.setLayoutParams(lp2);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}