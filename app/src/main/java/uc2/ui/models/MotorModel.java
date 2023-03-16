package uc2.ui.models;


import uc2.uc2rest.ApiServiceCallback;
import uc2.uc2rest.RestController;
import uc2.uc2rest.response.MotorGetResponse;

public class MotorModel {
    private Stepper stepperX;
    private Stepper stepperY;
    private Stepper stepperZ;
    private Stepper stepperA;

    private RestController restController;
    private ConnectionModel connectionModel;

    public MotorModel(RestController restController,ConnectionModel connectionModel)
    {
        this.restController = restController;
        this.connectionModel = connectionModel;
        stepperX = new Stepper(1,restController,connectionModel);
        stepperY = new Stepper(2,restController,connectionModel);
        stepperZ = new Stepper(3,restController,connectionModel);
        stepperA = new Stepper(0,restController,connectionModel);
    }

    public Stepper getStepperA() {
        return stepperA;
    }

    public Stepper getStepperX() {
        return stepperX;
    }

    public Stepper getStepperY() {
        return stepperY;
    }

    public Stepper getStepperZ() {
        return stepperZ;
    }

}
