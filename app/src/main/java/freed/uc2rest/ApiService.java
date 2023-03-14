package freed.uc2rest;


import freed.uc2rest.response.LedArrRequest;
import freed.uc2rest.response.LedArrResponse;
import freed.uc2rest.response.MacRequest;
import freed.uc2rest.response.MotorActRequest;
import freed.uc2rest.response.MotorGetResponse;
import freed.uc2rest.response.WifiConnectRequest;
import freed.uc2rest.response.items.BtScanItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/features_get")
    Call<String[]> getFeatures();

    @GET("/ledarr_get")
    Call<LedArrResponse> ledGet();

    @POST("/ledarr_act")
    Call<String> ledAct(@Body LedArrRequest request);

    @GET("/wifi/scan")
    Call<String[]> getSsids();

    @Headers("Content-Type: application/json")
    @POST("/wifi/connect")
    Call<String> connectToWifi(@Body WifiConnectRequest wifiConnectRequest);

    @GET("/resetnv")
    Call<Void> resetNvFlash();

    @GET("/bt_scan")
    Call<BtScanItem[]> scanForBtDevices();

    @GET("/bt_paireddevices")
    Call<BtScanItem[]> getPairedDevices();
    
    @POST("/bt_connect")
    Call<Void>connectToBtDevice(@Body MacRequest mac);
    @POST("/bt_paireddevices")
    Call<Void>removePairedDevice(@Body MacRequest mac);

    @GET("motor_get")
    Call<MotorGetResponse>getMotorData();

    @POST("motor_act")
    Call<Void>setMotorData(@Body MotorActRequest request);

}
