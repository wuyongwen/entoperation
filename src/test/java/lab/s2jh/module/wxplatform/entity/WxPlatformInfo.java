package lab.s2jh.module.wxplatform.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
/**
 * 第三方开发平台验证信息
 * @author wuyongwen
 * @Date 2016年3月17日下午5:23:55
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_platform_info", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "微信开发平台信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxPlatformInfo extends BaseNativeEntity {

	private static final long serialVersionUID = -8475122368055463304L;
	// 在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。
	@MetaData(value = "验证码")
	@Column(length = 300)
	private String componentVerifyTicket;

	@MetaData(value = "验证码推送时间")
	@Column(length = 100)
	private Long ticketCreateTime;

	// 第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌（component_access_token）。
	// 每个令牌是存在有效期（2小时）的，且令牌的调用不是无限制的，请第三方平台做好令牌的管理，在令牌快过期时（比如1小时50分）再进行刷新。
	@MetaData(value = "令牌")
	@Column(length = 300)
	private String componentAccessToken;

	@MetaData(value = "令牌失效时间")
	@Column(length = 300)
	private Long tokenExpiresOut;
	// 预授权码用于公众号授权时的第三方平台方安全验证。"expires_in":600
	@MetaData(value = "预授权码")
	@Column(length = 300)
	private String preAuthCode;

	@MetaData(value = "预授权码失效时间")
	@Column(length = 300)
	private Long preAuthCodeExpiresOut;

	public void setTokenExpiresOut(Long expiresIn) {
		this.tokenExpiresOut = System.currentTimeMillis() + (expiresIn - 600) * 1000;
	}

	public void setPreAuthCodeExpiresOut(Long authCodeExpiresOut) {
		this.preAuthCodeExpiresOut = System.currentTimeMillis() + (authCodeExpiresOut - 120) * 1000;
	}

	/**
	 * 平台令牌是否失效
	 * 
	 * @return
	 */
	public boolean isTokenExpiresOut() {
		return System.currentTimeMillis() > this.tokenExpiresOut;
	}
	/**
	 * 预授权码是否失效
	 * @return
	 */
	public boolean isPreAuthCodeExpiresOut() {
		return System.currentTimeMillis() > this.preAuthCodeExpiresOut;
	}
}
