package com.jb4dc.sso.webserver.rest;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.*;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhuangrb
 * Date: 2019/7/15
 * To change this template use File | Settings | File Templates.
 */
public class SendFile {
    @Test
    /*@DisplayName("When zero operands")*/
    public void testSendFile() throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:1708/")
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        //Call<ResponseBody> call = service.getWeatherData("北京");
        //Response<ResponseBody> result=call.execute();
        //System.out.println(result.body().string());
        //System.out.println(call.execute().isSuccessful());

        File file=new File("D:\\稿纸.pdf");
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        String descriptionString = "hello, 这是文件描述";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.getWeatherData(description, body);
        call.execute();

        /*call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //System.out.println("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Log.e("Upload error:", t.getMessage());
            }
        });*/
        call.execute();
    }
}

interface WeatherService{

    @Multipart
    @POST("OAWebService.asmx/SendFile")
    Call<ResponseBody> getWeatherData(@Part("description") RequestBody description,
                                      @Part MultipartBody.Part file);
}