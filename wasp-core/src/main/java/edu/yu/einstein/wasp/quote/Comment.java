package edu.yu.einstein.wasp.quote;

/**
 * 
 * @author dubin
 *
 */

public class Comment {
	private String comment;
	private String error;
	
	public Comment(){
		this.comment = "";
		this.error = "";
	}
	public Comment(String comment, String error){
		this.comment = comment;
		this.error = error;
	}
	public Comment(String comment){
		this.comment = comment;
		this.error = "";
	}
	public String getComment(){
		return this.comment;
	}
	public String getError(){
		return this.error;
	}
	public void setComment(String comment){
		this.comment=comment;
	}
	public void setError(String error){
		this.error = error;
	}
}
