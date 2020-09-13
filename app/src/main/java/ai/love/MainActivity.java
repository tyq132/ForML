package ai.love;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;

import ai.love.fragments.QuestionFragment;

public class MainActivity extends AppCompatActivity {
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
