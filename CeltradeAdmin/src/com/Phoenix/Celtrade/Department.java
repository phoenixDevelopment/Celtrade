package com.Phoenix.Celtrade;


public class Department {
	
	private int depID;
	private String depName;
	
	
	public Department(int id,String dep){
		this.depID = id;
		this.depName = dep;
	}
	
	public Department(String depName){
		this.depName = depName;
	}
	
	public int getDepID() {
		return depID;
	}
	
	public void setDepID(int depID) {
		this.depID = depID;
	}
	
	public String getDepName() {
		return depName;
	}
	
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
