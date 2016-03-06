package com.entgroup.wxplatform.entoperation.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entgroup.wxplatform.entoperation.domain.WxMpAPP;

@Repository
@Qualifier(value = "wxMpAPPRepository")
public interface WxMpAPPRepository extends CrudRepository<WxMpAPP, Long> {
	public WxMpAPP findByAppId(String appId);
}
