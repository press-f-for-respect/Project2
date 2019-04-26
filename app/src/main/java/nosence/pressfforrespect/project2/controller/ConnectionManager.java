package nosence.pressfforrespect.project2.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import nosence.pressfforrespect.project2.model.Comment;
import nosence.pressfforrespect.project2.model.Post;

public class ConnectionManager {
    private Context context;
    private MessageController messageController;

    public ConnectionManager(Context context, MessageController messageController){
        this.context = context;
        this.messageController = messageController;
    }

    public void loadPosts(){
        final Gson gson = new Gson();
        final ArrayList<Post> posts = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://jsonplaceholder.typicode.com/posts";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Post post = gson.fromJson(response.get(i).toString(), Post.class);
                        posts.add(post);
                    }
                    handlePosts(posts, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    posts.clear();
                    handlePosts(posts, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                posts.clear();
                handlePosts(posts, false);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void handlePosts(ArrayList<Post> posts, boolean succeed){
        //TODO for kourosh kun to complete
        if(succeed)
            messageController.updatePosts(posts);
        else
            messageController.fromCache(true);
    }

    public void loadComments(Context context, int postId){
        final Gson gson = new Gson();
        final ArrayList<Comment> comments = new ArrayList<>();
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://jsonplaceholder.typicode.com/comments?postId=" + postId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Comment comment = gson.fromJson(response.get(i).toString(), Comment.class);
                        comments.add(comment);
                    }
                    handleComments(comments, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                    comments.clear();
                    handleComments(comments, false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                comments.clear();
                handleComments(comments, false);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void handleComments(ArrayList<Comment> comments, boolean succeed){
        //TODO for kourosh kun to complete
        if(succeed)
            messageController.updateComments(comments);
        else
            messageController.fromCache(false);
    }
}
