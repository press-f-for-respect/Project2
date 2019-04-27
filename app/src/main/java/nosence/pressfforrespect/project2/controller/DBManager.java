package nosence.pressfforrespect.project2.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import nosence.pressfforrespect.project2.model.Comment;
import nosence.pressfforrespect.project2.model.Post;

public class DBManager {
    private SQLiteDatabase db;
    private static DBManager dbManager;

    public static DBManager getInstance(SQLiteDatabase db){
        if(dbManager == null)
            dbManager = new DBManager(db);
        return dbManager;

    }

    public static DBManager getInstance(){
        return dbManager;

    }

    private DBManager(SQLiteDatabase db) {
        this.db = db;
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = \"POSTS\"",null);
        if(cursor.getCount() == 0) {
            this.db.execSQL("CREATE TABLE POSTS (id INTEGER PRIMARY KEY, userid INTEGER, title TEXT, body TEXT)");
            this.db.execSQL("CREATE TABLE COMMENTS(id INTEGER PRIMARY KEY, postid INTEGER, name TEXT, email TEXT, body TEXT)");
        }
        cursor.close();
    }

    public void savePosts(ArrayList<Post> posts) {

        for(Post post: posts){
            db.delete("POSTS", "id = " + post.getId(), null);
            db.execSQL("INSERT INTO POSTS(id, userid, title, body) VALUES (" +post.getId() + ", " +
                    post.getUserId() + ", '" + post.getTitle() + "', '" + post.getBody() + "')");
        }
    }

    public void saveComments(ArrayList<Comment> comments) {
        for(Comment comment: comments) {
            db.delete("COMMENTS", "id = " + comment.getId(), null);
            db.execSQL("INSERT INTO COMMENTS(id, postid, name, email, body) VALUES (" +comment.getId() + ", " +
                    comment.getPostId() + ", '" + comment.getName() + "', '" + comment.getEmail() + "', '" + comment.getBody() + "')");
        }
    }

    public ArrayList<Post> getPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM POSTS",null);
        if (cursor.moveToFirst()){
            do{
                Post post = new Post(cursor.getInt(1), cursor.getInt(0), cursor.getString(2), cursor.getString(3));
                posts.add(post);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return posts;
    }

    public ArrayList<Comment> getComments(int postid) {
        ArrayList<Comment> comments = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Comments WHERE postid = " + postid, null);
        if (cursor.moveToFirst()){
            do{
                Comment comment = new Comment(cursor.getInt(1), cursor.getInt(0),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                comments.add(comment);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return comments;
    }

}
