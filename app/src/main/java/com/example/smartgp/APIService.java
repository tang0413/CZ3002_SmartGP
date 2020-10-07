package com.example.smartgp;

import com.example.smartgp.Notification.MyResponse;
import com.example.smartgp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAtZXfaWs:APA91bHZ0UD_oQBZi7QT6cAkvBmLB3zRjd6-8zpTUsG0zZgr76OqfhBBynI_AZrKIYJO2_5qckUJoYCTzEsOfQZ6ETv0dmnA3qiFfd1LZFM59LHrFivZ0W8fXGSJS8PZdCzmRAIO0Enu"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
