package edu.yu.einstein.wasp;

import java.util.Date;

import edu.yu.einstein.wasp.model.User;

/**
 * Encapsulates metadata messages for attaching to entities e.g. facility manager notes and status reports from wasp-daemon
 * @author andymac
 *
 */
public class MetaMessage{
	
	private String group;
	
	private String name;
	
	private String value;
	
	private String uniqueKey;
	
	private Date date;
	
	private User user;

	public MetaMessage(String uniqueKey, String group, String name, String value) {
		this.uniqueKey = uniqueKey;
		this.group = group;
		this.name = name;
		this.value = value;
		this.date = new Date();
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return "[ uniqueKey=" + uniqueKey + ", group=" + group + ", name=" + name + ", value=" + value + ", date=" + date + ", user=" + user + "]";
	}
	
}