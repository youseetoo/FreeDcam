package uc2.ui.models;

import android.view.View;
import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.troop.freedcam.BR;

import uc2.uc2rest.RestController;
import uc2.uc2rest.response.MotorActRequest;
import uc2.uc2rest.response.items.MotorActItem;


public class Stepper extends BaseObservable {
    String step_pin = "0";
    String dir_pin = "0";
    String power_pin = "0";

    boolean step_inverted = false;
    boolean dir_inverted = false;
    boolean power_inverted = false;
    private int id;
    private final int speedRanges[] = {-4000, -2000,-1000,-500,-200,-100,-50,-20,-10,-5,-4,-3,-2,-1,0,1,2,3,4,5,10,20,50,100,200,500,1000,2000, 4000};
    private int speedPos = getDefaultPosition();
    private boolean stopMotorOnSeekbarRelease = false;


    String position = "0";

    private RestController restController;
    private ConnectionModel connectionModel;

    public Stepper(int id, RestController restController,ConnectionModel connectionModel)
    {
        this.id = id;
        this.restController = restController;
        this.connectionModel = connectionModel;
    }

    @Bindable
    public String getStep_pin() {
        return step_pin;
    }

    public void setStep_pin(String step_pin) {
        if (this.step_pin == step_pin)
            return;
        this.step_pin = step_pin;
        notifyPropertyChanged(BR.step_pin);
    }
    @Bindable
    public String getDir_pin() {
        return dir_pin;
    }

    public void setDir_pin(String dir_pin) {
        if (this.dir_pin == dir_pin)
            return;
        this.dir_pin = dir_pin;
        notifyPropertyChanged(BR.dir_pin);
    }
    @Bindable
    public String getPower_pin() {
        return power_pin;
    }

    public void setPower_pin(String power_pin) {
        if (this.power_pin == power_pin)
            return;
        this.power_pin = power_pin;
        notifyPropertyChanged(BR.power_pin);
    }


    public void setDir_inverted(boolean dir_inverted) {
        if (this.dir_inverted == dir_inverted)
            return;
        this.dir_inverted = dir_inverted;
        notifyPropertyChanged(BR.dir_inverted);
    }
    @Bindable
    public boolean isDir_inverted() {
        return dir_inverted;
    }

    public void setPower_inverted(boolean power_inverted) {
        if (this.power_inverted = power_inverted)
            return;
        this.power_inverted = power_inverted;
        notifyPropertyChanged(BR.power_inverted);
    }
    @Bindable
    public boolean isPower_inverted() {
        return power_inverted;
    }

    public void setStep_inverted(boolean step_inverted) {
        if (this.step_inverted == step_inverted)
            return;
        this.step_inverted = step_inverted;
        notifyPropertyChanged(BR.step_inverted);
    }

    @Bindable
    public boolean isStep_inverted() {
        return step_inverted;
    }

    public void setStopMotorOnSeekbarRelease(boolean stopMotorOnSeekbarRelease) {
        if (stopMotorOnSeekbarRelease == this.stopMotorOnSeekbarRelease)
            return;
        this.stopMotorOnSeekbarRelease = stopMotorOnSeekbarRelease;
    }

    @Bindable
    public boolean getStopMotorOnSeekbarRelease() {
        return stopMotorOnSeekbarRelease;
    }


    public void setPosition(String position) {
        if (position == this.position)
            return;
        this.position = position;
    }

    @Bindable
    public String getPosition() {
        return position;
    }

    public void driverForward()
    {
        setSpeedPos(getSpeedRangesSize());
    }

    public void stopDrive()
    {
        setSpeedPos(getDefaultPosition());
    }

    public void driveBackward()
    {
       setSpeedPos(0);
    }

    @Bindable
    public int getSpeedRangesSize()
    {
        return speedRanges.length-1;
    }

    @Bindable
    public int getDefaultPosition()
    {
        return speedRanges.length/2;
    }

    public void setSpeedPos(int speedPos) {
        if (speedPos == this.speedPos)
            return;
        this.speedPos = speedPos;
        notifyPropertyChanged(BR.speedPos);
        notifyPropertyChanged(BR.speedString);

        MotorActRequest request = new MotorActRequest();
        MotorActItem item = new MotorActItem();
        request.motorActItem = new MotorActItem[1];
        request.motorActItem[0] = item;
        item.stepperid = id;
        item.speed = (long) speedRanges[speedPos];
        item.maxspeed = 20000L;
        if (item.speed != 0)
            item.isforever = 1;
        else item.isforever =0;
        connectionModel.sendSocketMessage(request);
        /*if (restController.getRestClient() != null)
            restController.getRestClient().setMotorData(request, new ApiServiceCallback<Void>() {
                @Override
                public void onResponse(Void response) {

                }
            });*/
    }

    @Bindable
    public int getSpeedPos() {
        return speedPos;
    }

    @Bindable
    public int getVisibility()
    {
        if (!step_pin.equals("0"))
            return View.VISIBLE;
        else
            return View.GONE;
    }

    @Bindable
    public String getSpeedString()
    {
        return "" + speedRanges[speedPos];
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (stopMotorOnSeekbarRelease)
            setSpeedPos(getDefaultPosition());
    }

}
