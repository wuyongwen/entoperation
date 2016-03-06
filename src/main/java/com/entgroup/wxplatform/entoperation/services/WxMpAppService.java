package com.entgroup.wxplatform.entoperation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.entgroup.wxplatform.entoperation.domain.WxMpAPP;
import com.entgroup.wxplatform.entoperation.repositories.WxMpAPPRepository;

@Service
public class WxMpAppService extends BaseServiceImp<WxMpAPP, Long>  implements
		BaseService<WxMpAPP, Long> {
	@Autowired
	private WxMpAPPRepository repository;
	public WxMpAPP findByAppId(String appId){
		return repository.findByAppId(appId);
	}
	@Override
	public CrudRepository<WxMpAPP, Long> getRepository() {
		return repository;
	}
}
