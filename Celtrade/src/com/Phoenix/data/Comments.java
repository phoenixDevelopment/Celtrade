package com.Phoenix.data;

import java.sql.Timestamp;
import java.util.Date;

public class Comments {
	private User user;
	private String comment;
	private Date commentedAt;
	
	public Comments(User user,String comment, Date commentdAt){
		this.user = user;
		this.comment = comment;
		this.commentedAt= commentdAt;
	}
	public String getComment() {
		return comment;
	}
	
	public User getUser() {
		return user;
	}
	
	public Date getCommentedAt() {
		return commentedAt;
	}
	
	public void setCommentedAt(Date commentedAt) {
		this.commentedAt = commentedAt;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
