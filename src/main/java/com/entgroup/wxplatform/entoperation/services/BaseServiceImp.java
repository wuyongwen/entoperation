package com.entgroup.wxplatform.entoperation.services;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public abstract class BaseServiceImp<T, ID extends Serializable> implements BaseService<T, ID> {
	public abstract CrudRepository<T,ID> getRepository();
	@Override
	public T getBeanById(ID id) {
		return getRepository().findOne(id);
	}

	@Override
	public void saveBean(T t) {
		getRepository().save(t);
	}

	@Override
	public void deleteBean(ID id) {
		getRepository().delete(id);
	}

}
