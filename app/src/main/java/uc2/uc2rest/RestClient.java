package uc2.uc2rest;

import android.util.Log;

import uc2.uc2rest.response.LedArrRequest;
import uc2.uc2rest.response.LedArrResponse;
import uc2.uc2rest.response.MacRequest;
import uc2.uc2rest.response.MotorActRequest;
import uc2.uc2rest.response.MotorGetResponse;
import uc2.uc2rest.response.WifiConnectRequest;
import uc2.uc2rest.response.items.BtScanItem;
import uc2.uc2rest.ws.Uc2WebSocket;


public class RestClient {

    ApiService apiService;
    String url;
    public RestClient(String url)
    {
        this.url = url;
        apiService = ApiServiceGenerator.createService(ApiService.class,"http://"+url+"/");
    }

    public Uc2WebSocket createWebSocket()
    {
        return new Uc2WebSocket(ApiServiceGenerator.getSharedClient(),"ws://"+url+":81");
    }

    public String[] getFeatures() {
        return ApiServiceGenerator.executeSync(apiService.getFeatures());
    }

    public void getFeaturesAsync(ApiServiceCallback<String[]> callback)
    {
        apiService.getFeatures().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getSsids(ApiServiceCallback<String[]> callback)
    {
        apiService.getSsids().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToWifi(WifiConnectRequest wifiConnectRequest, ApiServiceCallback<String> callback)
    {
        Log.d(RestClient.class.getSimpleName(), wifiConnectRequest.toString());
        apiService.connectToWifi(wifiConnectRequest).enqueue(new ApiServiceCallbackAdapter<String>(callback));
    }

    public void resetNvFLash(ApiServiceCallback<Void> callback)
    {
        apiService.resetNvFlash().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void setLedArr(LedArrRequest request, ApiServiceCallback<String> callback)
    {
        apiService.ledAct(request).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getLedConfig(ApiServiceCallback<LedArrResponse> c)
    {
        apiService.ledGet().enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void scanForBtDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        apiService.scanForBtDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void getPairedBTDevices(ApiServiceCallback<BtScanItem[]> callback)
    {
        apiService.getPairedDevices().enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void connectToBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        apiService.connectToBtDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }

    public void removePairedBtDevice(MacRequest mac, ApiServiceCallback<Void> callback)
    {
        apiService.removePairedDevice(mac).enqueue(new ApiServiceCallbackAdapter<>(callback));
    }


    public void getMotorData(ApiServiceCallback<MotorGetResponse> c)
    {
        apiService.getMotorData().enqueue(new ApiServiceCallbackAdapter<>(c));
    }

    public void setMotorData(MotorActRequest request, ApiServiceCallback<Void> c)
    {
        apiService.setMotorData(request).enqueue(new ApiServiceCallbackAdapter<>(c));
    }
}
