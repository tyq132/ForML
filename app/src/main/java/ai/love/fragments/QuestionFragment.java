package ai.love.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.classichu.lineseditview.LinesEditView;

import ai.love.R;
import ai.love.activity.DoneActivity;
import cn.refactor.lib.colordialog.PromptDialog;


/**
 *
 */
public class QuestionFragment extends Fragment {

    public static LinesEditView letter;
    public static EditText lover;
    public Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);
        letter = v.findViewById(R.id.edit_letter);
        lover = v.findViewById(R.id.edit_lover);
        button = v.findViewById(R.id.btn_commit);
        initButtonClickLinstener(getContext());
        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initButtonClickLinstener(final Context context) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = letter.getContentText();
                String str2 = lover.getText().toString();
                String level = null;
                if( str1 != null && str2.length()>1 ) {
                    if(str2.contains("唐")){
                        level = "^_^";
                    }
                    PromptDialog promptDialog = new PromptDialog(context)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText(getString(R.string.success)+level)
                            .setContentText(getString(R.string.text)+"\n\b提交码：0x"+System.currentTimeMillis())
                            .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                    playMusic();
                                    startActivity(new Intent(getActivity(), DoneActivity.class));
                                    getActivity().finish();
                                }
                            });
                    promptDialog.setCancelable(false);
                    promptDialog.show();
                }else {
                    Toast.makeText(context,"请填必填项哦~",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void playMusic() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.courage_liangjingru);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getContext(),"谢谢体验~",Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
}

