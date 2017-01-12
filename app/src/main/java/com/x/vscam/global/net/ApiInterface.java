package com.x.vscam.global.net;

import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.upload.UploadResponseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
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
    Call<ImgFlowBean> getImgFlow(@Query("s") int lastUnix);

    @Multipart
    @POST("/x/?a=upload")
    Observable<UploadResponseBean> uploadImg(@Part("pp\"; filename=\"image.jpg") RequestBody imgs);

}
