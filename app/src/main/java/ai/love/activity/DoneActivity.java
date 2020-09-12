package ai.love.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import ai.love.R;
import ai.love.utils.StatusBarUtil;

public class DoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        StatusBarUtil.immersive(this);

    }
}