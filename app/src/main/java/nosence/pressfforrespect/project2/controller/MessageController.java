package nosence.pressfforrespect.project2.controller;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

import nosence.pressfforrespect.project2.model.Comment;
import nosence.pressfforrespect.project2.model.Post;

public class MessageController {

    private NotificationCenter notificationCenter;
    private StorageManager storageManager;
    private ConnectionManager connectionManager;
    private ArrayList<Post> listOfPosts = new ArrayList<>();
    private ArrayList<Comment> listOfComments = new ArrayList<>();
    private static MessageController messageController;
    private DispatchQueue cloud = new DispatchQueue("cloud");
    private DispatchQueue storage = new DispatchQueue("storage");
    private Bundle bundle;

    public static MessageController getInstance(NotificationCenter notificationCenter, Context context, Bundle savedInstanceState){
        if(messageController == null)
            messageController = new MessageController(notificationCenter, context, savedInstanceState);
        return messageController;

    }

    public static MessageController getInstance(){
        return messageController;

    }

    public MessageController(NotificationCenter notificationCenter, Context context, Bundle savedInstanceState) {
        this.notificationCenter = notificationCenter;
        storageManager = new StorageManager(context, this);
        connectionManager = new ConnectionManager(context, this);
        this.bundle = savedInstanceState;
    }

    public ArrayList<Post> getListOfPosts() {
        return listOfPosts;
    }

    public ArrayList<Comment> getListOfComments() { return listOfComments; }

    public void fetchPosts() {
        cloud.postRunnable(new Runnable() {
            @Override
            public void run() {
                connectionManager.loadPosts();
            }
        });
    }

    public void fetchComments(final Context context, final int id) {
        cloud.postRunnable(new Runnable() {
            @Override
            public void run() {
                connectionManager.loadComments(context, id);
            }
        });
    }

    synchronized void updatePosts(ArrayList<Post> toAppendList) {
        listOfPosts.addAll(toAppendList);
        notificationCenter.dataLoaded(0);
    }

    synchronized void updateComments(ArrayList<Comment> toAppendList){
        listOfComments.addAll(toAppendList);
        notificationCenter.dataLoaded(1);
    }

    public void fromCache(boolean isPost){
        if(isPost)
            storage.postRunnable(new Runnable() {
                @Override
                public void run() {
                    storageManager.loadPosts();
                }
            });
        else
            storage.postRunnable(new Runnable() {
                @Override
                public void run() {
                    storageManager.loadComments();
                }
            });
    }
}
