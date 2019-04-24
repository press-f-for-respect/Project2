package nosence.pressfforrespect.project2.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nosence.pressfforrespect.project2.CommentsActivity;
import nosence.pressfforrespect.project2.R;
import nosence.pressfforrespect.project2.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_post, parent, false);
        return new PostViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.postTitle.setText(post.getTitle());
        holder.postBody.setText(post.getBody());
        holder.postId = post.getId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView postTitle, postBody;
        public int postId;
        public static final String POST_ID = "post id";


        public PostViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            postTitle = itemView.findViewById(R.id.post_title);
            postBody = itemView.findViewById(R.id.post_body);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra(POST_ID, postId);
            context.startActivity(intent);
        }
    }
}
