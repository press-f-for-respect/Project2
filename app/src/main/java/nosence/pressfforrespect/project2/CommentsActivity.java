package nosence.pressfforrespect.project2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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
import java.util.List;

import nosence.pressfforrespect.project2.controller.MessageController;
import nosence.pressfforrespect.project2.controller.NotificationCenter;
import nosence.pressfforrespect.project2.model.Comment;
import nosence.pressfforrespect.project2.model.Post;
import nosence.pressfforrespect.project2.utils.CommentAdapter;
import nosence.pressfforrespect.project2.utils.PostAdapter;

public class CommentsActivity extends AppCompatActivity implements NotificationCenter.Observer {

    private NotificationCenter notificationCenter = NotificationCenter.getInstance();
    private MessageController messageController;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Comment> comments;
    private DividerItemDecoration dividerItemDecoration;
    private Toolbar commentsToolbar;
    private ProgressDialog progressDialog;
    private int postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        notificationCenter.register(this);
        messageController = MessageController.getInstance();

        commentsToolbar = findViewById(R.id.comments_toolbar);
        commentsToolbar.setTitle("comments");
        setSupportActionBar(commentsToolbar);
        Intent intent = getIntent();
        postId = intent.getIntExtra(PostAdapter.PostViewHolder.POST_ID, 1);
        recyclerView = findViewById(R.id.comments_recycler_view);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(getApplicationContext(), comments);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        getComments();

    }

    private void getComments(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        messageController.fetchComments(getApplicationContext(), postId);
    }

    @Override
    public void update(int state) {
        if(state != 1)
            return;
        comments.clear();
        comments.addAll(messageController.getListOfComments());
        adapter.notifyDataSetChanged();
        commentsToolbar.setTitle("Post " + postId + ", " + comments.size() + " comments" );
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationCenter.unRegister(this);
    }
}
