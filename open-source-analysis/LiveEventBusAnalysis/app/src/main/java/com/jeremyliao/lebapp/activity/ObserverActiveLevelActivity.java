package com.jeremyliao.lebapp.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jeremyliao.lebapp.LiveEventBusDemo;
import com.jeremyliao.lebapp.R;
import com.jeremyliao.lebapp.databinding.ActivityObserverActiveLevelDemoBinding;
import com.jeremyliao.liveeventbus.LiveEventBus;


public class ObserverActiveLevelActivity extends AppCompatActivity {

    private ActivityObserverActiveLevelDemoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_observer_active_level_demo);
        binding.setLifecycleOwner(this);
        binding.setHandler(this);

        LiveEventBus.config(LiveEventBusDemo.KEY_TEST_ACTIVE_LEVEL_SINGLE)
                .lifecycleObserverAlwaysActive(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void sendMsgToPrevent() {
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_ACTIVE_LEVEL)
                .post("Send Msg To Observer Stopped");
    }

    public void sendMsgToPreventSingle() {
        LiveEventBus
                .get(LiveEventBusDemo.KEY_TEST_ACTIVE_LEVEL_SINGLE)
                .post("Send Msg To Observer Stopped");
    }
}
