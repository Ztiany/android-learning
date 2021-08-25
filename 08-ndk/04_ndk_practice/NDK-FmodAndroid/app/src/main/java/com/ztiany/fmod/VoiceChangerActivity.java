package com.ztiany.fmod;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.fmod.FMOD;

import java.lang.ref.WeakReference;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-08 12:24
 */
public class VoiceChangerActivity extends AppCompatActivity {

    private static final String FILE_NAME = "lzx.mp3";

    private static final int MESSAGE_PREPARE_PLAY = 10;
    private static final int MESSAGE_PLAY = 12;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_LUOLI = 1;
    private static final int MODE_DASHU = 2;
    private static final int MODE_JINGSONG = 3;
    private static final int MODE_GAOGUAI = 4;
    private static final int MODE_KONGLING = 5;

    private HandlerThread mHandlerThread;
    private PlayHandler mPlayHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FMOD.init(this);
        setContentView(R.layout.activity_voice_changer);

        mHandlerThread = new HandlerThread(this.getPackageName());
        mHandlerThread.start();
        mPlayHandler = new PlayHandler(this, mHandlerThread.getLooper());
    }

    private static class PlayHandler extends Handler {

        private final WeakReference<VoiceChangerActivity> mReference;

        private PlayHandler(VoiceChangerActivity activity, Looper looper) {
            super(looper);
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            removeMessages(MESSAGE_PLAY);
            removeMessages(MESSAGE_PREPARE_PLAY);

            if (msg.what == MESSAGE_PREPARE_PLAY) {

                Message obtain = Message.obtain();
                obtain.what = MESSAGE_PLAY;
                obtain.arg1 = msg.arg1;

                VoiceChangerActivity voiceChangerActivity = mReference.get();
                if (voiceChangerActivity != null) {
                    voiceChangerActivity.close();
                }

                sendMessageDelayed(obtain, 2000);

            } else if (msg.what == MESSAGE_PLAY) {
                VoiceChangerActivity voiceChangerActivity = mReference.get();
                if (voiceChangerActivity != null) {
                    voiceChangerActivity.playVoice(FILE_NAME, msg.arg1);
                }
            }
        }
    }

    public native void playVoice(String assetsName, int effect);

    public native void close();

    private void playVoice(int effect) {
        Message obtain = Message.obtain();
        obtain.arg1 = effect;
        obtain.what = MESSAGE_PREPARE_PLAY;
        mPlayHandler.sendMessage(obtain);
    }

    public void normalPlay(View view) {
        playVoice(MODE_NORMAL);
    }

    public void llPlay(View view) {
        playVoice(MODE_LUOLI);
    }

    public void dsPlay(View view) {
        playVoice(MODE_DASHU);
    }

    public void jsPlay(View view) {
        playVoice(MODE_JINGSONG);
    }

    public void ggPlay(View view) {
        playVoice(MODE_GAOGUAI);
    }

    public void klPlay(View view) {
        playVoice(MODE_KONGLING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
        FMOD.close();
        mHandlerThread.quitSafely();
    }

    public void stop(View view) {
        close();
    }
}
