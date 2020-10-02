package ai.love;

import android.Manifest;
import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.liuguangqiang.cookie.CookieBar;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ai.love.utils.ClickListenerUtil;
import ai.love.utils.DiskLruCacheUtil;
import ai.love.utils.StatusBarUtil;
import ai.love.view.FViewView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class WelcomeActivity extends AppCompatActivity{

    private DiskLruCacheUtil mDiskCacheUtil;
    private volatile boolean isAllPermissionsChecked = false;
    private FingerprintIdentify mFingerprintIdentify;
    private BottomDialog dialog;

    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private final Timer timer = new Timer();

    private final static String[] mPermissions = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.USE_FINGERPRINT};

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
        btn.setOnClickListener(new ClickListenerUtil() {
            @Override
            public void onMultiClick(View v) {
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
        /*Intent intent = new Intent(this, LoginActivity.class);
        Long size =mDiskCacheUtil.getSize();
        Log.e("缓存大小：",""+size);
        if(size==0L){
            intent.putExtra("hasCache","false");
            startActivity(intent);
        }else{
            if(getSettingFingerPrint()){
                initFingerPrint();
            }else {
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            //intent.putExtra("hasCache","true");
        }*/
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean getSettingFingerPrint() {

        SharedPreferences pref = getSharedPreferences("fingerprint_data",MODE_PRIVATE);
        System.out.println("指纹"+"："+pref.getBoolean("use_fingerprint",false));
        return pref.getBoolean("use_fingerprint",false);//第二个参数为默认值
    }

    private void initFingerPrint() {
        initFingerprintIdentify();
        initFingerprintIdentifyCallBack();
        dialog = new BottomDialog.Builder(WelcomeActivity.this)
                .setTitle("请验证指纹!")
                .setIcon(R.drawable.finger_print)
                .setContent("将手指放置在设备指纹验证区域即可验证身份信息.")
                .setPositiveText("OK")
                .setNegativeText("取消")
                .setPositiveBackgroundColorResource(R.color.colorPrimary)
                //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                .setPositiveTextColorResource(android.R.color.white)
                //.setPositiveTextColor(ContextCompat.getColor(this, android.R.color.colorPrimary)
                .setPositiveText("使用密码")
                .onPositive(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(BottomDialog dialog) {
                        Toast.makeText(WelcomeActivity.this,"请输入密码测试",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .onNegative(new BottomDialog.ButtonCallback() {
                    @Override
                    public void onClick(@NonNull BottomDialog bottomDialog) {
                        Log.e("BottomDialogs", "取消");
                        bottomDialog.dismiss();
                        onBackPressed();
                    }
                }).autoDismiss(false)
                .show();
    }

    /*初始化指纹验证*/
    private void initFingerprintIdentify() {
        mFingerprintIdentify = new FingerprintIdentify(this);       // create object
        mFingerprintIdentify.setSupportAndroidL(true);              // support android L
        mFingerprintIdentify.init();
        if(mFingerprintIdentify.isFingerprintEnable()&&mFingerprintIdentify.isHardwareEnable()){
            if(mFingerprintIdentify.isRegisteredFingerprint()){

                mFingerprintIdentify.startIdentify(5, new BaseFingerprint.IdentifyListener() {
                    @Override
                    public void onSucceed() {
                        // succeed, release hardware automatically
                        dialog.dismiss();
                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onNotMatch(int availableTimes) {
                        // not match, try again automatically
                        Log.e("指纹","onNotMatch:"+availableTimes);
                        new CookieBar.Builder(WelcomeActivity.this)
                                .setTitle("指纹错误！！！")
                                .setIcon(R.drawable.finger_error)
                                .setBackgroundColor(R.color.color_login_button)
                                .setMessage("还剩下"+availableTimes+"次机会")
                                .setDuration(1000)
                                .setLayoutGravity(Gravity.TOP)
                                .show();
                    }

                    @Override
                    public void onFailed(boolean isDeviceLocked) {
                        // failed, release hardware automatically
                        // isDeviceLocked: is device locked temporarily
                        Log.e("指纹","onFailed:"+isDeviceLocked);
                        if(isDeviceLocked){
                            dialog.dismiss();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onStartFailedByDeviceLocked() {
                        // the first start failed because the device was locked temporarily
                        Log.e("指纹","onStartFailedByDeviceLocked");

                    }
                });
            }
        }

    }
    /*指纹异常回调*/
    private void initFingerprintIdentifyCallBack() {
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.e("FingerprintIdentify", "Error__"+ Objects.requireNonNull(exception.getMessage()));
            }
        });
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