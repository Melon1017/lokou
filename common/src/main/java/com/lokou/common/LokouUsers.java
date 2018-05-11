package com.lokou.common;
import java.util.ArrayList;
import java.util.List;

public class LokouUsers {
	List<User> users;
	private LokouUsers(){
		users=new ArrayList<User>();
		String userStr=LokouPropertis.getLokouPropertis().getProperty("users").toString();  
		if(userStr!=null&&!userStr.equals("")){
			String[] userArray=userStr.split("\\^");
			for(String userInfo:userArray){
				User user=new User();
				String userName=userInfo.replaceAll("/.*", "");
				String password=userInfo.replaceAll(".*/", "");
				user.setUserName(userName);
				user.setPassword(SHA1.encode(password));
				users.add(user);
			}
		}else {
			User user=new User();
			user.setUserName("lokou");
			user.setPassword(SHA1.encode("lokou"));
			users.add(user);
		}
		
	}
	
	 private static	class  LokouUsersHolder{
		public static LokouUsers  instance=new LokouUsers();
	}
	 
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public static LokouUsers getLokouUsers(){
		
		return LokouUsersHolder.instance;
		
	}
	
	public static boolean userExists(String userName){
		for(User user:LokouUsersHolder.instance.users){
			  if(user.getUserName().equals(userName)){
				  return true;
			  }
		}
		return false;
	}
	public static User getUser(String userName){
		for(User user:LokouUsersHolder.instance.users){
			  if(user.getUserName().equals(userName)){
				  return user;
			  }
		}
		return null;
	}
}
