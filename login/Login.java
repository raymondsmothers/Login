package login;

import java.util.ArrayList;
import java.util.List;

import com.data.access.objects.UserDAO;

import login.SecurityEncryption;

public class Login {
	List<UserDAO> userList = new ArrayList<UserDAO>();
	SecurityEncryption se = new SecurityEncryption();
	
	public Login()
	{
		passwordList = se.hashPasswordList(passwordList);
	}
	
	public boolean checkCredentials(String user, String pass) 
	{
		String temp = se.toHexString(se.getSHA(pass));
		for(int i = 0; i < usernameList.length; i++)
		{
			if(user.equals(usernameList[i]) && temp.equals(passwordList[i]))
			{
				return true;
			}
		}
		
		return false;
	}
}
