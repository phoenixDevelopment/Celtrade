package com.Phoenix.data;

public class User {

	private int userID;
	private String userName;
	private String depName;
	
	
	public User(int userID,String name) {
		// TODO Auto-generated constructor stub
		this.userID = userID;
		this.userName = name;
	}
	
	public User(int userID,String name,String dep) {
		// TODO Auto-generated constructor stub
		this.userID = userID;
		this.userName = name;
		this.depName = dep;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
		this.userID = 0;
		this.userName = "";
	}

	public int getUserID() {
		return userID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getDepName() {
		return depName;
	}
}
