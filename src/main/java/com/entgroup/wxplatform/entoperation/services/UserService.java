package com.entgroup.wxplatform.entoperation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.entgroup.wxplatform.entoperation.domain.User;
import com.entgroup.wxplatform.entoperation.repositories.UserRepository;

@Service
public class UserService extends BaseServiceImp<User, Long>  implements
		BaseService<User, Long> {
	@Autowired
	private UserRepository repository;
	public User findByUsername(String username){
		return repository.findByUsername(username);
	}
	@Override
	public CrudRepository<User, Long> getRepository() {
		return repository;
	}
}
