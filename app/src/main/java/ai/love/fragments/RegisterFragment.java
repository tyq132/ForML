package ai.love.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ai.love.R;

/**
 * Created by James Tang on 2020/8/29
 */
public class RegisterFragment extends Fragment {

    View form_register, imglogo, darkoverlay;
    public static EditText userName, passward, key;
    public static TextView gotoLogin;
    private DisplayMetrics dm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_register, container, false);
        dm=getResources().getDisplayMetrics();
        form_register=v.findViewById(R.id.form_register);
        imglogo=v.findViewById(R.id.fragmentregisterLogo);
        darkoverlay=v.findViewById(R.id.fragmentregisterView1);
        userName = v.findViewById(R.id.username_register);
        passward = v.findViewById(R.id.pass_register);
        key = v.findViewById(R.id.key);
        gotoLogin = v.findViewById(R.id.goto_login);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imglogo.animate().setStartDelay(700).setDuration(2000).alpha(1).start();
        imglogo.animate().translationY(-dm.heightPixels).setStartDelay(0).setDuration(0).start();
        imglogo.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(0).start();
        darkoverlay.animate().setStartDelay(600).setDuration(2000).alpha(0.6f).start();
        form_register.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
        form_register.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(0).start();
    }

    public void executeAnimation() {
        imglogo.animate().translationY(0).setStartDelay(0).setDuration(0).start();
        imglogo.animate().translationY(-dm.heightPixels).setDuration(1500).alpha(1).setStartDelay(0).start();
        form_register.animate().translationY(0).setDuration(0).alpha(1).start();
        form_register.animate().translationY(-dm.heightPixels).setStartDelay(100).setDuration(1500).start();
    }
}
