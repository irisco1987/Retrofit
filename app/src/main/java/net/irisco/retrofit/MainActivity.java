package net.irisco.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView txt_content;
    PlaceHolderJsonApi placeHolderJsonApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_content = findViewById(R.id.txt_content);


        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request orginalRequest = chain.request();

                        Request newRequest = orginalRequest.newBuilder()
                                .addHeader("Interceptor-Header", "xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Gson gson = new GsonBuilder().serializeNulls().create();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(GsonConverterFactory.create(gson))

                .client(okHttpClient)
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .build();

        placeHolderJsonApi = retrofit.create(PlaceHolderJsonApi.class);

        //  getComments();
        //  getPost();
        //  createPost();
        updatePost();
        //  deletePost();
    }

    private void deletePost() {
        final Call<Void> Call = placeHolderJsonApi.deletePosts(10);
        Call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {

                txt_content.setText(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {

            }
        });
    }

    private void updatePost() {
        Post post = new Post(210, null, "body_test");
        // Call<Post> Call = placeHolderJsonApi.PutPost(2, post);
        //  Call<Post> Call = placeHolderJsonApi.PatchPost(2, post);
        //  Call<Post> Call = placeHolderJsonApi.PutPost("abc",2, post);

        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "efg");
        headers.put("header2", "hig");

        Call<Post> Call = placeHolderJsonApi.PatchPost(headers, 2, post);
        Call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    String content = "";
                    Post post = response.body();
                    content +=
                            "Code :" + response.code() + "\n" +
                                    " ID :" + post.getId() + "\n"
                                    + "USER ID : " + post.getUserId() + "\n"
                                    + " Title :" + post.getTitle() + "\n"
                                    + " Text :" + post.getText() + "\n\n";

                    txt_content.setText(content);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {

            }
        });

    }

    private void createPost() {
        Post post = new Post(100, "test_title", "hello world");
        //  Call<Post> Call = placeHolderJsonApi.createPost(post);
        //  Call<Post> Call = placeHolderJsonApi.createPost(2000, "test_title", "test_body");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "205");
        fields.put("title", "test_title");
        fields.put("body", "body_test");

        Call<Post> Call = placeHolderJsonApi.createPost(fields);
        Call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(retrofit2.Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    String content = "";
                    Post post = response.body();

                    content +=
                            "Code :" + response.code() + "\n" +
                                    " ID :" + post.getId() + "\n"
                                    + "USER ID : " + post.getUserId() + "\n"
                                    + " Title :" + post.getTitle() + "\n"
                                    + " Text :" + post.getText() + "\n\n";

                    txt_content.setText(content);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Post> call, Throwable t) {

            }
        });
    }

    private void getComments() {
        //  Call<List<Comment>> Call=placeHolderJsonApi.getComments(3);
        Call<List<Comment>> Call = placeHolderJsonApi.getComments("posts/{id}/comments");
        Call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    String content = "";
                    List<Comment> comments = response.body();
                    for (Comment comment : comments) {
                        content += " PosID : " + comment.getPostId() + "\n" + " Name :" + comment.getName() + "\n" + " Email :" + comment.getEmail() + "\n" + " Text :" + comment.getText() + "\n\n";
                    }
                    txt_content.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
            }
        });
    }

    private void getPost() {
        //  Call<List<Post>> Call =placeHolderJsonApi.getPost(new Integer[]{1,3}, "id","desc");
        Map<String, String> parameter = new HashMap<>();
        parameter.put("userId", "1");
        parameter.put("_sort", "id");
        parameter.put("_order", "desc");


        Call<List<Post>> Call = placeHolderJsonApi.getPost(parameter);
        Call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    String content = "";

                    List<Post> postList = response.body();
                    for (Post post : postList) {
                        content += " ID :" + post.getId() + "\n" + "USER ID : " + post.getUserId() + "\n" + " Title :" + post.getTitle() + "\n" + " Text :" + post.getText() + "\n\n";
                    }
                    txt_content.setText(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
