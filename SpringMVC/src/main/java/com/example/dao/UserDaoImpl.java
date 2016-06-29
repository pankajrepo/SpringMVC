package com.example.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.model.User;

@Repository
public class UserDaoImpl implements UserDao{

	private static List<User> userList= new ArrayList<User>();
	//private static Map<String, User> userList= new HashMap<String, User>();
	
	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		System.out.println(id);
		System.out.println(userList.size());
		
		if(userList.size()==0)
			return null;
		else
			return userList.get(--id);
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userList;
	}

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		//userList.put(user.getName(), user);
		userList.add(user);
	}

	@Override
	public void update(User user) {
		userList.get(user.getId()).setName(user.getName());
		userList.get(user.getId()).setAddress(user.getAddress());
		userList.get(user.getId()).setCountry(user.getCountry());
		userList.get(user.getId()).setEmail(user.getEmail());
		
	}

	@Override
	public void delete(Integer id) {
		for(Iterator<User> user= userList.iterator();user.hasNext();)
		{
			User userObj= user.next();
			if(userObj.getId() == id)
			{
				user.remove();
			}
		}
		
	}

}
