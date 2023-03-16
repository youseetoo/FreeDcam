package uc2.ui;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import uc2.uc2rest.RestController;
import uc2.ui.models.ConnectionModel;
import uc2.ui.models.LedModel;
import uc2.ui.models.MotorModel;

@HiltViewModel
public class Uc2ViewModel extends ViewModel implements DefaultLifecycleObserver {

    private MotorModel motorModel;
    private LedModel ledModel;
    private ConnectionModel connectionModel;

    public MotorModel getMotorModel() {
        return motorModel;
    }
    public LedModel getLedModel() {
        return ledModel;
    }
    public ConnectionModel getConnectionModel() {
        return connectionModel;
    }

    @Inject
    public Uc2ViewModel(RestController restController)
    {
        connectionModel = new ConnectionModel(restController);
        motorModel = new MotorModel(restController,connectionModel);
        ledModel = new LedModel(restController,connectionModel);
    }



    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.d("Uc2ViewModel", "onResume");
        DefaultLifecycleObserver.super.onResume(owner);
        try {
            connectionModel.onConnectButtonClick();
            connectionModel.resumeWebSocket();
            ledModel.getLedSettings();
        }
        catch (IllegalArgumentException ex)
        {
            connectionModel.setMessage(ex.getLocalizedMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        connectionModel.pauseWebSocket();
        Log.d("Uc2ViewModel", "onPause");
    }

}
