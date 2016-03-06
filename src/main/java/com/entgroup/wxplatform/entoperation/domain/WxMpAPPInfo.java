package com.entgroup.wxplatform.entoperation.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.chn.wx.vo.result.PlatFormGetAuthorizerInfoResult;
import com.chn.wx.vo.result.PlatFormGetAuthorizerInfoResult.AuthorizerInfo;
/**
 * 公众号详细信息
 * @author wen
 *
 */
@Entity
@Table(name="wx_wxmpappinfo")
public class WxMpAPPInfo {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
	
	@Column(name="nick_name")
	private String nickName;
	@Column(name="head_img")
	private String headImg;
	@Column(name="servicetype_info")
	private Integer serviceTypeInfo;
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="app_id")
	private WxMpAPP app;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Integer getServiceTypeInfo() {
		return serviceTypeInfo;
	}
	public void setServiceTypeInfo(Integer serviceTypeInfo) {
		this.serviceTypeInfo = serviceTypeInfo;
	}
	public WxMpAPP getApp() {
		return app;
	}
	public void setApp(WxMpAPP app) {
		this.app = app;
	}
	public void setAuthorizerInfor(PlatFormGetAuthorizerInfoResult authorizeInfo) {
		AuthorizerInfo info = authorizeInfo.getAuthorizerInfo();
		this.nickName = info.getNickName();
		this.headImg = info.getHeadImg();
		this.serviceTypeInfo = info.getServiceTypeInfo().getId();
	}
	
}
