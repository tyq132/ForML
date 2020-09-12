package ai.love;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.liuguangqiang.cookie.CookieBar;
import com.liuguangqiang.cookie.OnActionClickListener;

import java.util.Timer;
import java.util.TimerTask;

import ai.love.fragments.LoginFragment;
import ai.love.fragments.RegisterFragment;
import ai.love.utils.DiskLruCacheUtil;
import ai.love.utils.StatusBarUtil;

public class LoginActivity extends AppCompatActivity {

    LoginFragment frag_login;
    RegisterFragment frag_register;
    KenBurnsView kbv;
    ProgressBar pbar, pbar2;
    View button_login, button_register, button_icon, sucess_icon;
    LinearLayout label_signup, label_login;
    TextView button_label, button_label_regis, btn_goto_register, btn_goto_Login;
    EditText userNmae, passward, key;
    ValueAnimator va;
    Context mContext;

    DiskLruCacheUtil diskLruCacheUtil;

    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        pbar = findViewById(R.id.mainProgressBar1);
        pbar2 = findViewById(R.id.mainProgressBar_regis);
        button_icon = findViewById(R.id.button_icon);
        sucess_icon = findViewById(R.id.button_icon_regis);
        kbv = findViewById(R.id.Login_Register_BG);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
        kbv.setTransitionGenerator(generator);
        diskLruCacheUtil = new DiskLruCacheUtil(getApplicationContext());
        button_label = findViewById(R.id.button_label);
        button_label_regis = findViewById(R.id.button_label_regis);

        dm = getResources().getDisplayMetrics();
        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        /*设置按钮偏移量*/
        button_register.setTranslationX(dm.widthPixels + button_register.getMeasuredWidth());
        btn_goto_register = findViewById(R.id.goto_register);
        btn_goto_Login = findViewById(R.id.goto_login);
        label_signup = findViewById(R.id.label_signup);
        label_login = findViewById(R.id.label_loginin);

        button_login.setTag(0);
        pbar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        /* 全屏显示*/
        StatusBarUtil.immersive(this);

        frag_login = new LoginFragment();
        frag_register = new RegisterFragment();

        /* 将登陆界面布局替换掉页面空白容器*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_login).commit();

        initLabel();
        initVaView();
        initLoginListener();
        initRegisterLoginListener();


    }

    /*初始化登陆、注册动画*/
    private void initLabel() {
        label_signup.animate().setStartDelay(600).setDuration(2000).alpha(1).start();
    }

    /*初始化背景*/
    private void initVaView() {
        va = new ValueAnimator();
        va.setDuration(1500);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator p1) {
                if (button_register.getTag().equals(3)) {
                    RelativeLayout.LayoutParams button_login_lp = (RelativeLayout.LayoutParams) button_register.getLayoutParams();
                    button_login_lp.width = Math.round((Float) p1.getAnimatedValue());
                    button_register.setLayoutParams(button_login_lp);
                } else if(button_register.getTag().equals(1)) {
                    RelativeLayout.LayoutParams button_login_lp = (RelativeLayout.LayoutParams) button_login.getLayoutParams();
                    button_login_lp.width = Math.round((Float) p1.getAnimatedValue());
                    button_login.setLayoutParams(button_login_lp);
                }
            }
        });
    }

    /*初始化登陆、注册lablel*/
    private void initRegisterLoginListener() {
        if (btn_goto_register != null) {
            btn_goto_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*设置底部登陆注册切换效果*/
                    label_signup.setVisibility(View.GONE);
                    label_signup.setAlpha(0);
                    label_login.animate().setStartDelay(750).setDuration(2000).alpha(1).start();
                    label_login.setVisibility(View.VISIBLE);
                    /*执行输入框动画*/
                    frag_login.executeAnimation();
                    /*执行按钮动画*/
                    executeBtnAnimation(button_login);
                    /* 将登陆界面布局替换掉页面空白容器*/
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_register).commit();
                        }
                    }, 1000);
                }
            });
        }
        if (btn_goto_Login != null) {
            btn_goto_Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*设置底部登陆注册切换效果*/
                    label_login.setVisibility(View.GONE);
                    label_login.setAlpha(0);
                    label_signup.animate().setStartDelay(750).setDuration(2000).alpha(1).start();
                    label_signup.setVisibility(View.VISIBLE);
                    /*执行输入框动画*/
                    frag_register.executeAnimation();
                    executeBtnAnimation(button_register);
                    /* 将登陆界面布局替换掉页面空白容器*/
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_login).commit();
                        }
                    }, 1000);
                }
            });
        }
    }

    /*执行按钮动画*/
    private void executeBtnAnimation(View btn) {
        button_register.setTag(0);
        button_login.setTag(0);
        if (btn == button_login) {
            button_login.animate().translationX(0).setStartDelay(500).start();
            button_login.animate().translationX(-1500).setDuration(1500).start();
            button_register.animate().translationX(dm.widthPixels + button_register.getMeasuredWidth()).setDuration(0).setStartDelay(500).start();
            button_register.animate().translationX(0).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        } else {
            button_register.animate().translationX(0).start();
            button_register.animate().translationX(dm.widthPixels + button_register.getMeasuredWidth()).setStartDelay(500).setDuration(1500).start();
            button_login.animate().translationX(-1500).setDuration(0).setStartDelay(100).start();
            button_login.animate().translationX(0).setStartDelay(500).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        }
    }

    /*初始化登陆/注册按钮*/
    private void initLoginListener() {
        /*展示动画*/
        button_login.animate().translationX(dm.widthPixels + button_login.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        button_login.animate().translationX(0).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
        button_login.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                button_login.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                button_login.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {

                if ((int) button_login.getTag() == 1) {
                    return;
                } else if (checkCache()) {
                    executeAnimation();
                    return;
                }
                userNmae = LoginFragment.userName;
                passward = LoginFragment.passward;
                //Toast.makeText(getApplicationContext(), userNmae + "" + passward, Toast.LENGTH_SHORT).show();

                if (userNmae != null && passward != null) {
                    login(userNmae.getText().toString(), passward.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "没有输入你的名字或者密码哟~", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNmae = RegisterFragment.userName;
                passward = RegisterFragment.passward;
                key = RegisterFragment.key;
                if (userNmae != null && passward != null && key != null) {
                    register(userNmae.getText().toString(), passward.getText().toString(), key.getText().toString());
                }
            }
        });
    }

    private boolean checkCache() {
        boolean hasCache = getIntent().getStringExtra("hasCache").equals("true");
        Log.e("login测试：",""+hasCache);
        if (hasCache) {
            return true;
        }
        return false;
    }

    /*登录*/
    private void login(String username, String pass) {
        if (username.length() > 5 && pass.length() > 5) {
            Log.e("登录验证：", "" + diskLruCacheUtil.getCache(username));
            if (pass.equals(diskLruCacheUtil.getCache(username))) {
                executeAnimation();
            } else {
                Toast.makeText(this, "仔细检查一下名字和密码吖~", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "用户名或密码不能为空哟~", Toast.LENGTH_SHORT).show();
        }

    }

    /*注册*/
    private void register(String userName, String pass, String key) {
        if (key.equals("qwe")) {
            if (userName.length() > 5 && pass.length() > 5) {
                /*判断用户名是否存在*/
                boolean hasSingup = !diskLruCacheUtil.getCache(userName).equals("NONE");
                Log.e("验证：", "has singup"+diskLruCacheUtil.getCache(userName));
                if(!hasSingup){
                    if (diskLruCacheUtil.addCache(userName, pass)) {
                        registerSucess();
                    }else{
                    }
                }else{
                    Toast.makeText(this, "已经注册过啦~小笨蛋！"+diskLruCacheUtil.getCache(userName), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "用户名、密码长度都要大于5哟~", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "密钥不对哟~", Toast.LENGTH_SHORT).show();
        }
        //registerSucess();
    }

    private void registerSucess() {

        button_register.setTag(3);
        va.setFloatValues(button_register.getMeasuredWidth(), button_register.getMeasuredHeight());
        va.start();
        pbar2.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
        button_label_regis.animate().setStartDelay(100).setDuration(500).alpha(0).start();
        button_register.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator p1) {
                if (button_register.getTag().equals(3)) {

                }
            }

            @Override
            public void onAnimationEnd(Animator p1) {
                final Timer timer = new Timer();
                Log.e("结束", "");
                if (button_register.getTag().equals(3)) {
                    button_register.setBackgroundResource(R.drawable.success_icon);
                    pbar2.setAlpha(0);
                    button_register.setClickable(false);
                    new CookieBar.Builder(LoginActivity.this)
                            .setTitle("成功注册啦！")
                            .setIcon(R.drawable.tip_success)
                            .setBackgroundColor(R.color.color_primary_men)
                            .setMessage("点击-->进入APP~").setMessageColor(R.color.red_btn_bg_color)
                            .setDuration(4000)
                            .setLayoutGravity(Gravity.BOTTOM)
                            .setActionWithIcon(R.drawable.next_step, new OnActionClickListener() {
                                @Override
                                public void onClick() {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    /*取消定时任务*/
                                    timer.cancel();
                                    finish();
                                }
                            })
                            .show();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);

                }
            }

            @Override
            public void onAnimationCancel(Animator p1) {
                // TODO: Implement this method
            }

            @Override
            public void onAnimationRepeat(Animator p1) {
                // TODO: Implement this method
            }
        }).start();
    }

    private void executeAnimation() {
        button_register.setTag(1);
        va.setTarget("login");
        va.setFloatValues(button_login.getMeasuredWidth(), button_login.getMeasuredHeight());
        va.start();
        pbar.animate().setStartDelay(300).setDuration(1000).alpha(1).start();
        button_label.animate().setStartDelay(100).setDuration(500).alpha(0).start();
        button_login.animate().setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(3000).setDuration(1000).scaleX(30).scaleY(30).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator p1) {
                pbar.animate().setStartDelay(0).setDuration(0).alpha(0).start();
            }

            @Override
            public void onAnimationEnd(Animator p1) {
                button_login.setClickable(true);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator p1) {
                // TODO: Implement this method
            }

            @Override
            public void onAnimationRepeat(Animator p1) {
                // TODO: Implement this method
            }
        }).start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}