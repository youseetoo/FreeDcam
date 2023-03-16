package uc2.ui.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.troop.freedcam.BR;

import uc2.uc2rest.ApiServiceCallback;
import uc2.uc2rest.RestController;
import uc2.uc2rest.enums.LedModes;
import uc2.uc2rest.response.LedArrRequest;
import uc2.uc2rest.response.LedArrResponse;
import uc2.uc2rest.response.items.LedColorItem;

public class LedModel extends BaseObservable {
    private RestController restController;
    private String ledcount = "64";
    private boolean leds_turned_on = false;
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private ConnectionModel connectionModel;

    public LedModel(RestController restController, ConnectionModel connectionModel)
    {
        this.restController = restController;
        this.connectionModel = connectionModel;
    }

    @Bindable
    public String getLedcount() {
        return ledcount;
    }

    public void setLedcount(String ledcount) {
        this.ledcount = ledcount;
        if (this.ledcount == ledcount)
            return;
        notifyPropertyChanged(BR.ledcount);
    }

    public void setLedsOn(boolean leds_turned_on) {

        if(this.leds_turned_on == leds_turned_on)
            return;
        this.leds_turned_on = leds_turned_on;
        notifyPropertyChanged(BR.ledsOn);
        sendLedEnableRequest();
    }

    @Bindable
    public boolean getLedsOn() {
        return leds_turned_on;
    }

    private void sendLedEnableRequest()
    {
        LedArrRequest request =new LedArrRequest();
        request.LEDArrMode = LedModes.full.ordinal();
        request.led_array =new LedColorItem[1];
        request.led_array[0] = new LedColorItem();
        request.led_array[0].id = 0;
        request.led_array[0].r = leds_turned_on ? red:0;
        request.led_array[0].g = leds_turned_on ? green:0;
        request.led_array[0].b = leds_turned_on ? blue:0;
        restController.getRestClient().setLedArr(request,setLedCallback);
    }

    private void updateColors()
    {
        LedArrRequest request =new LedArrRequest();
        request.LEDArrMode = LedModes.full.ordinal();
        request.led_array =new LedColorItem[1];
        request.led_array[0] = new LedColorItem();
        request.led_array[0].id = 0;
        request.led_array[0].r = red;
        request.led_array[0].g = green;
        request.led_array[0].b = blue;
        connectionModel.sendSocketMessage(request);
        //restController.getRestClient().setLedArr(request,setLedCallback);
    }

    private ApiServiceCallback<String> setLedCallback = new ApiServiceCallback<String>() {
        @Override
        public void onResponse(String response) {

        }
    };

    public void setRed(int red) {
        if (this.red == red)
            return;
        this.red = red;
        notifyPropertyChanged(BR.red);
        if (leds_turned_on)
            updateColors();
    }

    @Bindable
    public int getRed() {
        return red;
    }

    public void setBlue(int blue) {
        if (this.blue == blue)
            return;
        this.blue = blue;
        notifyPropertyChanged(BR.blue);
        if (leds_turned_on)
            updateColors();
    }

    @Bindable
    public int getBlue() {
        return blue;
    }

    public void setGreen(int green) {
        if (this.green == green)
            return;
        this.green = green;
        notifyPropertyChanged(BR.green);
        if (leds_turned_on)
            updateColors();
    }

    @Bindable
    public int getGreen() {
        return green;
    }

    private ApiServiceCallback<String> setLedConfigCallback = new ApiServiceCallback<String>() {
        @Override
        public void onResponse(String response) {

        }
    };

    public void getLedSettings()
    {
        if (restController.getRestClient() == null)
            return;
        restController.getRestClient().getLedConfig(getLedSettingsCallback);
    }

    private ApiServiceCallback<LedArrResponse> getLedSettingsCallback = new ApiServiceCallback<LedArrResponse>() {
        @Override
        public void onResponse(LedArrResponse response) {
            ledcount = ""+response.ledArrNum;
            leds_turned_on = response.is_on;
            notifyPropertyChanged(BR.ledsOn);
            notifyPropertyChanged(BR.ledcount);
        }
    };




}
