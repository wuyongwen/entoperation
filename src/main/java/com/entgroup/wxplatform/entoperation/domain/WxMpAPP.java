package com.entgroup.wxplatform.entoperation.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.chn.common.StringUtils;
import com.chn.wx.vo.result.PlatFormGetAuthInfoResult.AuthorizationInfo;

/**
 * 微信公众号bean
 * 
 * @author wen
 *
 */
@Entity
@Table(name = "wx_wxmpapp")
public class WxMpAPP {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	@Column(name = "app_id")
	private String appId;
	@Column(name = "access_token")
	private String accessToken;
	@Column(name = "expries_in")
	private long expriesIn;
	@Column(name = "refresh_token")
	private String refreshToken;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private WxMpAPPInfo infoDetial;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getExpriesIn() {
		return expriesIn;
	}

	public void setExpriesIn(long expriesIn) {
		this.expriesIn = expriesIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public WxMpAPPInfo getInfoDetial() {
		return infoDetial;
	}

	public void setInfoDetial(WxMpAPPInfo infoDetial) {
		this.infoDetial = infoDetial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
		this.accessToken = authorizationInfo.getAuthorizerAccessToken();
		this.appId = authorizationInfo.getAuthorizerAppId();
		String expiresIn = authorizationInfo.getExpiresIn();
		if (!StringUtils.isEmpty(expiresIn))
			this.expriesIn = System.currentTimeMillis()
					+ (Integer.parseInt(expiresIn) - 200) * 1000l;
		this.refreshToken = authorizationInfo.getAuthorizerRefreshToken();
	}
	public boolean isExpriesIn(){
		return System.currentTimeMillis() > this.expriesIn;
	}
}
