package login;

import login.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.data.access.objects.UserDAO;

class LoginTest {	
	SecurityEncryption se = new SecurityEncryption();
	TestingClass testClass = new TestingClass();
	Login login = new Login();
	
	@Test
	void credentialCheckTest()
	{
		System.out.println("***Beginning credentialsCheckTest***");
		System.out.println();
		
		System.out.println(login.checkCredentials("nothing", "password"));
		System.out.println(login.checkCredentials("admin", "admin"));
		System.out.println(login.checkCredentials("normal", "password"));
		System.out.println(login.checkCredentials("normal", "p@ssw0rd"));
		
		System.out.println();
		System.out.println("***Ending credentialsCheckTest***");
	}
	
	@Test
	void testParser()
	{
		System.out.println("***Beginning testParser***");
		System.out.println();
		
		
		List<UserDAO> userList = new ArrayList<UserDAO>();
		userList = se.parseConfig();
		
		for(int i = 0; i < userList.size(); i++)
			System.out.println(userList.get(i).getUsername() + " : " + userList.get(i).getPassword());
		
		System.out.println();
		System.out.println("***Ending testParser***");
	}
}
