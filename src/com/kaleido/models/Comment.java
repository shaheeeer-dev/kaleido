package com.kaleido.models;
import java.util.ArrayList;
import java.util.List;

public class Comment {
    private int id;
    private int postId;
    private int userId;
    private String text;

    private static List<Comment> comments = new ArrayList<>();

    public Comment(int id, int PostId, int UserId, String text) {
        this.id = id;
        this.postId = PostId;
        this.userId = UserId;
        this.text = text;
    }

    public int getId() { return id; }
    public int getPostId() { return postId; }
    public int getUserId() { return userId; }
    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                '}';
    }

    public static void addComment(Comment comment) {
        comments.add(comment);
        System.out.println("Comment added: " + comment.getText());
    }

    public static void deleteComment(int id) {
        boolean removed = comments.removeIf(c -> c.getId() == id);
        if (removed) {
            System.out.println("Comment with ID " + id + " deleted.");
        } else {
            System.out.println("Comment with ID " + id + " not found.");
        }
    }

    public static void editComment(int id, String newText) {
        for (Comment c : comments) {
            if (c.getId() == id) {
                c.setText(newText);
                System.out.println("Comment with ID " + id + " updated to: " + newText);
                return;
            }
        }
        System.out.println("Comment with ID " + id + " not found.");
    }

    public static void showComments() {
        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
        } else {
            System.out.println("All Comments:");
            for (Comment c : comments) {
                System.out.println(c);
            }
        }
    }
}
