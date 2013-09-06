package edu.yu.einstein.wasp.quote;

/**
 * 
 * @author dubin
 *
 */

public class Discount {
	private String reason;
	private String type; //a percent or a money value (such as $)
	private Float value;//such as 155.00 (as in $5.00) or 10 (as in 10%)
	private String error;
	
	public Discount(){
		this.reason="";
		this.type="";
		this.value=new Float(0);
		this.error="";
	}
	public Discount(String reason, String type, Float value, String error){
		this.reason=reason;
		this.type=type;
		this.value=value;
		this.error=error;
	}
	public Discount(String reason, String type, Float value){
		this.reason=reason;
		this.type=type;
		this.value=value;
		this.error="";
	}
	
	public void setReason(String reason){
		this.reason=reason;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setValue(Float value){
		this.value=value;
	}
	public void setError(String error){
		this.error=error;
	}
	public String getReason(){
		return reason;
	}
	public String getType(){
		return type;
	}
	public Float getValue(){
		return this.value;
	}
	public String getError(){
		return this.error;
	}
}
