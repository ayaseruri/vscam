package com.x.vscam.global.net;

import com.x.vscam.imgdetail.ImgDetailBean;
import com.x.vscam.imgdetail.MapBean;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.settings.UploadAvatarResponseBean;
import com.x.vscam.upload.SubImgBean;
import com.x.vscam.upload.UploadResponseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public interface ApiInterface {
    @GET("/x/?a=h")
    Call<ImgFlowBean> getImgFlow(@Query("s") int lastUnix, @Query("u") int uid);

    @Multipart
    @POST("/x/?a=upload")
    Observable<UploadResponseBean> uploadImg(@Part("pp\"; filename=\"image.jpg") RequestBody imgs);

    @Multipart
    @POST("/x/?a=avatar")
    Observable<UploadAvatarResponseBean> uploadAvatar(@Part("avatar\"; filename=\"image.jpg") RequestBody imgs);

    @GET("/x/?a=avatar.del")
    Observable<ResponseBody> delAvatar();

    @POST("/x/?a=release")
    Observable<UploadAvatarResponseBean> subImg(@Body SubImgBean subImgBean);

    @Multipart
    @POST("/x/?a=filter")
    Observable<ResponseBody> reportImg(@Part("pid")int pid, @Part("text")String reson);

    @GET("/x/?a=maps&size=800*300")
    Observable<MapBean> getMap(@Query("gps") String gps);

    @GET("/x/?a=p")
    Observable<ImgDetailBean> getImgDetail(@Query("id") int id);

    @FormUrlEncoded
    @POST("/x/?a=u")
    Observable<UserBean> login(@Field("id") String id, @Field("password") String pass);

    @FormUrlEncoded
    @POST("/x/?a=u`")
    Observable<UserBean> register(@Field("name") String nick, @Field("mail") String email, @Field("password") String
            pass);

    @FormUrlEncoded
    @POST("/x/?a=u")
    Observable<UserBean> editInfo(@Field("des") String des, @Field("url") String url);
}
