package it.platform.utility;

import java.util.Set;
import it.platform.model.Role;
import it.platform.model.User;

public class Utility {
	
	
	public static boolean isUserRole(User user) {
		Boolean message = false;
		Set<Role> roles = user.getRoles();
		for (Role roleFind : roles) {
			if (roleFind.getName().equals("USER")) {
				message = true;
				break;
			} 
		}
		return message;
	}
	
	public static boolean search(String name) {
    	boolean message = false;
		if (name != null) {
			message = true;
		}
		return message;
    }
	

}
