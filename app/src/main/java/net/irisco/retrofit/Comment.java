package net.irisco.retrofit;

import com.google.gson.annotations.SerializedName;

public class Comment {
    int postId;
    int id;
    String email;
    String name;
    @SerializedName("body")
    String text;

    public int getPostId() {
        return postId;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
