package com.x.vscam.global.net;

import java.util.Map;

import com.x.vscam.global.bean.UserBean;
import com.x.vscam.imgdetail.ImgDetailBean;
import com.x.vscam.imgdetail.MapBean;
import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.settings.SettingsBean;
import com.x.vscam.settings.UploadAvatarResponseBean;
import com.x.vscam.upload.UploadResponseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public interface ApiInterface {
    @Headers("referer:https://vscam.co")
    @GET("/x/?a=h")
    Call<ImgFlowBean> getImgFlow(@Query("s") int lastUnix, @Query("u") int uid);

    @Multipart
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=upload")
    Observable<UploadResponseBean> uploadImg(@Part("pp\"; filename=\"image.jpg") RequestBody imgs);

    @Multipart
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=avatar")
    Observable<UploadAvatarResponseBean> uploadAvatar(@Part("avatar\"; filename=\"image.jpg") RequestBody imgs);

    @Headers("referer:https://vscam.co")
    @GET("/x/?a=avatar.del")
    Observable<ResponseBody> delAvatar();

    @FormUrlEncoded
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=release")
    Observable<UploadAvatarResponseBean> subImg(@FieldMap Map<String, Object> subImg);

    @Multipart
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=filter")
    Observable<ResponseBody> reportImg(@Part("pid")int pid, @Part("text")String reson);

    @GET("/x/?a=maps&size=800*300")
    @Headers("referer:https://vscam.co")
    Observable<MapBean> getMap(@Query("gps") String gps);

    @Headers("referer:https://vscam.co")
    @GET("/x/?a=p")
    Observable<ImgDetailBean> getImgDetail(@Query("id") int id);

    @FormUrlEncoded
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=u")
    Observable<UserBean> login(@Field("id") String id, @Field("password") String pass);

    @FormUrlEncoded
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=u")
    Observable<UserBean> register(@Field("name") String nick, @Field("mail") String email, @Field("password") String
            pass);

    @FormUrlEncoded
    @Headers("referer:https://vscam.co")
    @POST("/x/?a=u")
    Observable<SettingsBean> editInfo(@Field("des") String des, @Field("url") String url);
}
