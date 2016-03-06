package com.entgroup.wxplatform.entoperation.services;


public interface BaseService<T, ID> {
	
	public T getBeanById(ID id);

	public void saveBean(T t);

	public void deleteBean(ID id);
}
