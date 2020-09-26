package ai.love.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.kevalpatel2106.fingerprintdialog.AuthenticationCallback;
import com.kevalpatel2106.fingerprintdialog.FingerprintDialogBuilder;

import java.util.Objects;

import ai.love.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by James Tang on 2020/9/26
 */
public class SettingFragment extends PreferenceFragmentCompat {

    SwitchPreferenceCompat fingerprintSwitch;
    AuthenticationCallback fingerPrintCallback;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        fingerprintSwitch = findPreference("fingerprint");
        SharedPreferences pref = requireActivity().getSharedPreferences("fingerprint_data",MODE_PRIVATE);
        fingerprintSwitch.setDefaultValue(pref.getBoolean("use_fingerprint",false));
        initFingerPrintSwitch();

    }

    private void initFingerPrintSwitch() {

        fingerprintSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean useFingerPrint = (boolean) newValue;
                if(useFingerPrint){
                    initFingerPrint();
                }else{
                    initPreferencesFingerPrint(false);
                }
                return true;
            }
        });
    }

    private void initFingerPrint() {
        fingerPrintCallback = new AuthenticationCallback() {
            @Override
            public void fingerprintAuthenticationNotSupported() {
                // Device doesn't support fingerprint authentication. May be device doesn't have fingerprint hardware or device is running on Android below Marshmallow.
                // Switch to alternate authentication method.
                Log.e("指纹验证", "不支持");
            }

            @Override
            public void hasNoFingerprintEnrolled() {
                // User has no fingerprint enrolled.
                // Application should redirect the user to the lock screen settings.
                // FingerprintUtils.openSecuritySettings(this)
                Log.e("指纹验证", "没有录入指纹");
            }

            @Override
            public void onAuthenticationError(final int errorCode, @Nullable final CharSequence errString) {
                // Unrecoverable error. Cannot use fingerprint scanner. Library will stop scanning for the fingerprint after this callback.
                // Switch to alternate authentication method.
                Log.e("错误码" + errorCode, "info:" + errString);
                if (errorCode == 5) {
                    fingerprintSwitch.setChecked(false);
                    initPreferencesFingerPrint(false);
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
                Log.e("指纹验证", "成功");
                initPreferencesFingerPrint(true);

            }

            @Override
            public void onAuthenticationFailed() {
                // Authentication failed.
                // Library will continue scanning the fingerprint after this callback.
                Log.e("指纹验证", "失败");
            }
        };
        FingerprintDialogBuilder dialogBuilder = new FingerprintDialogBuilder(requireContext())
                .setTitle("请验证指纹")
                .setSubtitle("验证指纹以开启指纹验证")
                .setDescription("开启后，打开本APP可通过指纹验证直接进入，提高安全性的同时更便捷")
                .setNegativeButton("取消");
        dialogBuilder.show(getActivity().getSupportFragmentManager(),fingerPrintCallback);
    }

    /*保存指纹验证设置*/
    private void initPreferencesFingerPrint(boolean useFingerprint) {
        SharedPreferences pref = requireActivity().getSharedPreferences("fingerprint_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("use_fingerprint",useFingerprint);
        editor.apply();
    }
}