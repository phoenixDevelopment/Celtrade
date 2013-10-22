package com.Phoenix.data;

import java.io.ObjectInputStream.GetField;
import java.util.Date;

public class OpenIssue extends Issue{

	private String openByUser;
	private String openForDep;
	private int openedAt;
	private String time;
	
	public OpenIssue() {
		// TODO Auto-generated constructor stub
	}
	
	public OpenIssue(String openUs,String openForD,int id,String name,String type,String prior,String stat,int openedAt){
		super(id,name, type, prior, stat);
		this.openByUser = openUs;
		this.openForDep = openForD;
		this.openedAt = openedAt;
	}
	
	public OpenIssue(String openUs,String openForD,int id,String name,String type,String prior,String stat,String openedAt){
		super(id,name, type, prior, stat);
		this.openByUser = openUs;
		this.openForDep = openForD;
		this.time = openedAt;
	}
	
	public String getOpenForDep() {
		return openForDep;
	}
	
	public String getOpenByUser() {
		return openByUser;
	}
	
	public int getOpenedAt() {
		return openedAt;
	}
	
	public void setOpenedAt(int openedAt) {
		this.openedAt = openedAt;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
}
