package net.irisco.retrofit;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface PlaceHolderJsonApi {
    @GET("posts")
    Call<List<Post>> getPost(@QueryMap Map<String, String> parameters);

    @GET("posts")
    Call<List<Post>> getPost(@Query("userId") Integer[] userId,
                             @Query("_sort") String sort,
                             @Query("_order") String order
    );

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);


    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );


    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> parameter);


    @DELETE("posts/{id}")
    Call<Void> deletePosts(@Path("id") int id);


@Headers({"Static-Heder: 123","Static-Header1: 456"})
    @PUT("posts/{id}")
    Call<Post> PutPost(@Header("Dynamic-Header") String header, @Path("id") int id, @Body Post post);


    @PATCH("posts/{id}")
    Call<Post> PatchPost(@HeaderMap Map<String,String> headers, @Path("id") int id, @Body Post post);
}
