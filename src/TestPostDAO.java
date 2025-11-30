package com.kaleido.app;

import com.kaleido.db.PostDAO;
import com.kaleido.models.Post;

import java.util.List;

public class TestPostDAO {
    public static void main(String[] args) {
        // In your test class
        PostDAO postDAO = new PostDAO();

// Test creating a post
        Post testPost = new Post();
        testPost.setUserId(12); // Use an existing user ID
        testPost.setContentText("9");
        testPost.setImageUrl("");

        boolean created = postDAO.createPost(testPost);
        System.out.println("Post created: " + created);

// Test retrieving posts
        List<Post> posts = postDAO.getAllPosts();
        System.out.println("Number of posts: " + posts.size());
    }
}
