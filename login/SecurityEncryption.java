package login;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.data.access.objects.*;

public class SecurityEncryption 
{
	private List<UserDAO> credentialList = new ArrayList<UserDAO>();
	
	//default constructor
	public SecurityEncryption()
	{
		
	}
	
	//add a single user to the list of users
	public SecurityEncryption(UserDAO user) 
	{
		putCredential(user);
	}
	
	//create a list of users from an already 
	//existing list of users
	public SecurityEncryption(List<UserDAO> userList)
	{
		for(int i = 0; i < userList.size(); i++)
			putCredential(userList.get(i));
	}
	
	public UserDAO putCredential(UserDAO user) 
	{
		user.setPassword(toHexString(getSHA(user.getPassword())));
		credentialList.add(user);
		
		return user;
	}
	
	public List<UserDAO> putCredential(List<UserDAO> userList) 
	{
		for(int i = 0; i < userList.size(); i++)
		{
			UserDAO currentUser = userList.get(i);
			currentUser.setPassword(toHexString(getSHA(currentUser.getPassword())));
			credentialList.add(currentUser);
		}
		
		
		return getCredentials();
	}
	
	//Return the list of passwords
	public List<UserDAO> getCredentials()
	{
		return credentialList;
	}
	
	//MD5 Hashing Algorithm Implementation
	//NOTE: MD5 is flawed as it is possible that the hashing algorithm
	//can be reversed engineered (NOT RECOMMENDED FOR USE)
	public byte[] getMD5(String input)
	{
		try
		{
			//get the hashing algorithm MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			//return the hashed array of bytes
			return md.digest(input.getBytes());
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//SHA-256 Hashing Algorithm Implementation
	public byte[] getSHA(String input)
	{
		try
		{
			//get the hashing algorithm SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			//return the hashed array of bytes
			return md.digest(input.getBytes());
		}
		catch(NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	//convert the resulting hash to a string of hexidecimal values
	public String toHexString(byte[] hash)
	{
		BigInteger num = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(num.toString(16));
		
		while(hexString.length() < 32)
		{
			hexString.insert(0, "0");
		}
		
		return hexString.toString();
	}
	
	public String[] hashPasswordList(String[] passList)
	{
		for(int i = 0; i < passList.length; i++)
		{
			try
			{
				passList[i] = toHexString(getSHA(passList[i]));
			}
			catch(Exception e)
			{
				passList[i] = "";
			}
		}
		
		return passList;
	}
	
	public List<UserDAO> parseConfig() {
		JSONParser parser = new JSONParser();
		List<UserDAO> userDaoList = new ArrayList<UserDAO>();
		try 
		{
			JSONArray a = (JSONArray) parser.parse(new FileReader("./config.json"));
			
			for(Object o : a) 
			{
				JSONObject user = (JSONObject) o;
				UserDAO userDao = new UserDAO();
				
				String username = user.get("username").toString();
				userDao.setUsername(username);
				
				String password = user.get("password").toString();
				userDao.setPassword(password);
				
				userDaoList.add(userDao);
			}
			
		}
		catch(FileNotFoundException f)
		{
			System.out.println("Reading from config failed...");
		}
		catch(ParseException p)
		{
			System.out.println(p);
			System.out.println(p.getMessage());
		}
		catch(IOException i)
		{
			System.out.println("IO Failure...");
		}
		putCredential(userDaoList);
		
		return getCredentials();
	}
	
	public boolean checkCredentials(String user, String pass) 
	{
		String temp = toHexString(getSHA(pass));
		pass = null;
		List<UserDAO> userList = getCredentials();
		
		for(int i = 0; i < userList.size(); i++)
		{
			if(user.equals(userList.get(i).getUsername()) && temp.equals(userList.get(i).getPassword()))
			{
				return true;
			}
		}
		
		return false;
	}
}
