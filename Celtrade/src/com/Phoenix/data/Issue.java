package com.Phoenix.data;

import java.util.ArrayList;

public class Issue {

	private int issueID;
	private String issueName;
	private String issueType;
	private String priority;
	private String status;
	private ArrayList<Comments> comment = new ArrayList<Comments>();
	
	public Issue() {
		// TODO Auto-generated constructor stub
	}
	
	public Issue(int id,String name,String type,String prior,String stat){
		this.issueID = id;
		this.issueName = name;
		this.issueType = type;
		this.priority = prior;
		this.status = stat;
	}
	
	public String getIssueName() {
		return issueName;
	}
	
	public String getIssueType() {
		return issueType;
	}
	
	public String getPriority() {
		return priority;
	}
	public String getStatus() {
		return status;
	}
	
	public int getIssueID() {
		return issueID;
	}
	
	public ArrayList<Comments> getComment() {
		return comment;
	}
	
	public void setComment(ArrayList<Comments> comment) {
		this.comment = comment;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public void addComment(Comments cmt){
		this.comment.add(cmt);
	}
}
