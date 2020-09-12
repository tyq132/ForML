package ai.love.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ai.love.R;

public class LoginFragment extends Fragment {
	View form_login, imglogo, label_signup, darkoverlay;
	public static EditText userName,passward;
	private DisplayMetrics dm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_login, container, false);
		dm=getResources().getDisplayMetrics();
		form_login=v.findViewById(R.id.form_login);
		imglogo=v.findViewById(R.id.fragmentloginLogo);
		darkoverlay=v.findViewById(R.id.fragmentloginView1);
		userName = v.findViewById(R.id.username);
		passward = v.findViewById(R.id.passward);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		imglogo.animate().setStartDelay(700).setDuration(2000).alpha(1).start();
		imglogo.animate().translationY(-dm.heightPixels).setStartDelay(0).setDuration(0).start();
		imglogo.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(500).start();
		darkoverlay.animate().setStartDelay(600).setDuration(2000).alpha(0.6f).start();
		form_login.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
		form_login.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(500).start();
	}

	public void executeAnimation(){
		/*generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
		kbv.setTransitionGenerator(generator);
		imglogo.animate().setStartDelay(700).setDuration(2000).alpha(1).start();
		darkoverlay.animate().setStartDelay(600).setDuration(2000).alpha(0.6f).start();*/
		imglogo.animate().translationY(0).setStartDelay(0).setDuration(0).start();
		imglogo.animate().translationY(-dm.heightPixels).setDuration(1500).alpha(1).setStartDelay(0).start();
		form_login.animate().translationY(0).setStartDelay(0).setDuration(0).start();
		form_login.animate().translationY(-dm.heightPixels).setDuration(1500).alpha(1).setStartDelay(100).start();
	}
}

