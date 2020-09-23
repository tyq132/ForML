package ai.love;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.kevalpatel2106.fingerprintdialog.AuthenticationCallback;
import com.kevalpatel2106.fingerprintdialog.FingerprintDialogBuilder;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;

import ai.love.fragments.QuestionFragment;

public class MainActivity extends AppCompatActivity {

    /*指纹验证回调*/
    private AuthenticationCallback fingerPrintCallback;

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
            R.drawable.bee,
            R.drawable.butterfly,
    };
    public static FrameLayout frameLayout;
    private Fragment currentFragment,nextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*状态栏透明*/
        /*Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLUE);
        }
        setContentView(R.layout.main_layout);
        initCallBack();
        initFingerprintIdentify();
        frameLayout = findViewById(R.id.menu_fragments);
        currentFragment = new QuestionFragment();
        /*显示fragment*/
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragments, currentFragment).commit();
        initToolBar();

        /*初始化Boom button*/
        BoomMenuButton bmb = findViewById(R.id.bmb);
        initBmb(bmb);
        initBmbClickListener(bmb);
    }

    private void initCallBack() {
        fingerPrintCallback = new AuthenticationCallback() {
            @Override
            public void fingerprintAuthenticationNotSupported() {
                // Device doesn't support fingerprint authentication. May be device doesn't have fingerprint hardware or device is running on Android below Marshmallow.
                // Switch to alternate authentication method.
                Log.e("指纹验证","不支持");
            }

            @Override
            public void hasNoFingerprintEnrolled() {
                // User has no fingerprint enrolled.
                // Application should redirect the user to the lock screen settings.
                // FingerprintUtils.openSecuritySettings(this)
                Log.e("指纹验证","没有录入指纹");
            }

            @Override
            public void onAuthenticationError(final int errorCode, @Nullable final CharSequence errString) {
                // Unrecoverable error. Cannot use fingerprint scanner. Library will stop scanning for the fingerprint after this callback.
                // Switch to alternate authentication method.
                Log.e("错误码"+errorCode,"info:"+errString);
                if(errorCode == 5){
                    onBackPressed();
                }
            }

            @Override
            public void onAuthenticationHelp(final int helpCode, @Nullable final CharSequence helpString) {
                // Authentication process has some warning. such as "Sensor dirty, please clean it."
                // Handle it if you want. Library will continue scanning for the fingerprint after this callback.
            }

            @Override
            public void authenticationCanceledByUser() {
                // User canceled the authentication by tapping on the cancel button (which is at the bottom of the dialog).
            }

            @Override
            public void onAuthenticationSucceeded() {
                // Authentication success
                // Your user is now authenticated.
                Log.e("指纹验证","成功");
            }

            @Override
            public void onAuthenticationFailed() {
                // Authentication failed.
                // Library will continue scanning the fingerprint after this callback.
                Log.e("指纹验证","失败");
            }
        };
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        final DrawerLayout drawerLayout = findViewById(R.id.draw_layout);
        NavigationView nav = findViewById(R.id.navView);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        /*取消menu item为灰色*/
        nav.setItemIconTintList(null);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
               /* drawerLayout.closeDrawers();*/
                Toast.makeText(getApplicationContext(),"撒即可得分",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initBmb(BoomMenuButton bmb) {
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            switch (i) {
                case 0:
                    HamButton.Builder builder = new HamButton.Builder()
                            .normalImageRes(imageResources[0])
                            .normalText("功能1")
                            .subNormalText("Little butter Doesn't fly, either!");
                    bmb.addBuilder(builder);
                    break;
                case 1:
                    HamButton.Builder builder1 = new HamButton.Builder()
                            .normalImageRes(imageResources[1])
                            .normalText("功能2")
                            .subNormalText("Little butter Doesn't fly, either!");
                    bmb.addBuilder(builder1);
                    break;
                case 2:
                    HamButton.Builder builder2 = new HamButton.Builder()
                            .normalImageRes(imageResources[2])
                            .normalText("功能3")
                            .subNormalText("Little butter Doesn't fly, either!");
                    bmb.addBuilder(builder2);
                    break;
                case 3:
                    HamButton.Builder builder3 = new HamButton.Builder()
                            .normalImageRes(imageResources[3])
                            .normalText("功能4")
                            .subNormalText("Little butter Doesn't fly, either!");
                    bmb.addBuilder(builder3);
                    break;
            }
        }
    }

    private void initBmbClickListener(BoomMenuButton bmb) {
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                if(index == 0){
                    System.out.println("点击了0");
                }else if(index == 1){
                    FingerprintDialogBuilder dialogBuilder = new FingerprintDialogBuilder(MainActivity.this)
                            .setTitle("请验证指纹")
                            .setSubtitle("验证指纹以解相关内容")
                            .setDescription("骑个马上打款了法国今年科技馆虐生日就给你发的结果那没事的开发解放路快递公司考量的")
                            .setNegativeButton("取消");
                    dialogBuilder.show(getSupportFragmentManager(),fingerPrintCallback);

                }
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }

    private void initFingerprintIdentify() {
        FingerprintDialogBuilder dialogBuilder = new FingerprintDialogBuilder(MainActivity.this)
                .setTitle("请验证指纹")
                .setSubtitle("验证指纹以解相关内容")
                .setDescription("骑个马上打款了法国今年科技馆虐生日就给你发的结果那没事的开发解放路快递公司考量的")
                .setNegativeButton("取消");
        dialogBuilder.show(getSupportFragmentManager(),fingerPrintCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_right:
                Toast.makeText(this,"右边的应用菜单",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



}
