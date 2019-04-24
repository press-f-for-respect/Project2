package nosence.pressfforrespect.project2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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

import nosence.pressfforrespect.project2.model.Post;
import nosence.pressfforrespect.project2.utils.MembersListDialogFragment;
import nosence.pressfforrespect.project2.utils.PostAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private List<Post> posts;
    private DividerItemDecoration verticalDividerItemDecoration;
    private DividerItemDecoration horizontalDividerItemDecoration;
    private Toolbar mainToolbar;
    private int listState = 0;
    private MembersListDialogFragment membersListDialogFragment = new MembersListDialogFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        recyclerView = findViewById(R.id.posts_recycler_view);
        posts = new ArrayList<>();
        adapter = new PostAdapter(getApplicationContext(), posts);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        verticalDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        horizontalDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(verticalDividerItemDecoration);
        recyclerView.setAdapter(adapter);
        getPosts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_to_grid:
                if(listState == 0) {
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.addItemDecoration(horizontalDividerItemDecoration);
                    listState = 1;
                } else {
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.removeItemDecoration(horizontalDividerItemDecoration);
                    listState = 0;
                }
                return true;
            case R.id.members_list:
                membersListDialogFragment.show(getFragmentManager(), "members");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    private void getPosts() {
        final Gson gson = new Gson();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}
