package nosence.pressfforrespect.project2.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nosence.pressfforrespect.project2.R;
import nosence.pressfforrespect.project2.model.Comment;
import nosence.pressfforrespect.project2.model.Post;

public class StorageManager {

    private Context context;
    private String filename;
    private MessageController messageController;
    private DBManager dbManager;

    public StorageManager(Context context, MessageController messageController){
        this.context = context;
        this.messageController = messageController;
        this.filename = context.getString(R.string.file_addr);
        this.dbManager = DBManager.getInstance();
    }

    public int read(){
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
            }
            fileInputStream.close();
            return Integer.parseInt(sb.toString());
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return -1;
    }


    public void loadPosts(){
        messageController.updatePosts(dbManager.getPosts());
    }

    public void loadComments(int postid){
        messageController.updateComments(dbManager.getComments(postid));
    }

    public void save(int last){
        String fileContents = Integer.toString(last);
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}