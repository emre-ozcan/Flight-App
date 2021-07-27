package com.emreozcan.flightapp.data.network

import com.emreozcan.flightapp.models.notification.PushNotification
import com.emreozcan.flightapp.util.Constants.Companion.CONTENT_TYPE
import com.emreozcan.flightapp.util.Constants.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

}