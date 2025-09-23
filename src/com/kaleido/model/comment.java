package com.kaleido.model;
import java.util.ArrayList;
import java.util.List;

public class comment {
    private int id;
    private int PostId;
    private int UserId;
    private String text;

    private static List<comment> comments = new ArrayList<>();

    public comment(int id, int PostId, int UserId, String text) {
        this.id = id;
        this.PostId = PostId;
        this.UserId = UserId;
        this.text = text;
    }

    public int getId() { return id; }
    public int getPostId() { return PostId; }
    public int getUserId() { return UserId; }
    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + PostId +
                ", userId=" + UserId +
                ", text='" + text + '\'' +
                '}';
    }

    public static void addComment(comment comment) {
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
        for (comment c : comments) {
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
            for (comment c : comments) {
                System.out.println(c);
            }
        }
    }
}
