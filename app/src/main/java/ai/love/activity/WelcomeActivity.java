package ai.love.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.liuguangqiang.cookie.CookieBar;

import java.util.Timer;
import java.util.TimerTask;

import ai.love.LoginActivity;
import ai.love.MainActivity;
import ai.love.R;
import ai.love.utils.DiskLruCacheUtil;
import ai.love.utils.StatusBarUtil;
import ai.love.view.FViewView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class WelcomeActivity extends AppCompatActivity{

    private DiskLruCacheUtil mDiskCacheUtil;
    private volatile boolean isAllPermissionsChecked = false;

    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private final Timer timer = new Timer();

    private final static String[] mPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private volatile boolean btnIsClick;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mDiskCacheUtil = new DiskLruCacheUtil(this);
        /*全屏显示*/
        StatusBarUtil.immersive(this);

        /*申请权限*/
        initPermissions();

        initBtnNext();

        initVideos();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                jumpToLogin();
            }
        },15000);
        /*跳转到登录界面*/

    }

    private void initVideos() {
        FViewView videoView = findViewById(R.id.video);
        //设置视频源播放res/raw中的文件,文件名小写字母,格式: 3gp,mp4等,flv的不一定支持;
        Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.king_video);
        videoView.setVideoURI(rawUri);
        videoView.start();
        //videoView.requestFocus();

    }

    private void initBtnNext() {
        final Button btn = findViewById(R.id.btn_next);
        btn.animate().translationX(getResources().getDisplayMetrics().widthPixels + btn.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        btn.animate().translationX(0).setStartDelay(500).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        btn.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                new CookieBar.Builder(WelcomeActivity.this)
                        .setTitle("好久不见！")
                        .setIcon(R.drawable.seal)
                        .setBackgroundColor(R.color.color_primary_men)
                        .setMessage("点击-->进入APP~").setMessageColor(R.color.color_login_button)
                        .setDuration(3000)
                        .setLayoutGravity(Gravity.BOTTOM)
                        .show();
                btn.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setClickable(false);
                /*取消定时跳转*/
                timer.cancel();
                /*跳转至登录界面*/
                jumpToLogin();
            }
        });

    }

    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, mPermissions, 1);
        }
    }

    private void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        String temp =mDiskCacheUtil.getCache("tangyq");
        Log.e("welcome测试：",""+temp);
        if(temp.equals("NONE")){
            intent.putExtra("hasCache","false");
        }else{
            intent = new Intent(this, MainActivity.class);
            //intent.putExtra("hasCache","true");
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDiskCacheUtil.closeCache();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    isAllPermissionsChecked = true;
                } else {
                    isAllPermissionsChecked = false;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, 2);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(WelcomeActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }
}