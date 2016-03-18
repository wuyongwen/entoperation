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
 * 微信第三方开发账号的配置信息
 * @author wuyongwen
 * @Date 2016年3月17日下午4:12:37
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_platform_conf", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "微信开发平台配置信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxPlatformConf extends BaseNativeEntity {
	private static final long serialVersionUID = 8019726231382255899L;
	@MetaData(value = "应用Id")
	@Column(length = 100, nullable = false)
	private String appId;
	@MetaData(value = "应用名称")
	@Column(length = 50)
	private String appName;
	@MetaData(value = "秘钥")
	@Column(length = 200, nullable = false)
	private String secret;
	@MetaData(value = "加密秘钥")
	@Column(length = 200, nullable = false)
	private String aeskey;
	@MetaData(value = "令牌")
	@Column(length = 50, nullable = false)
	private String token;
	@MetaData(value = "授权回调地址")
	@Column(length = 500, nullable = false)
	private String loginRedirectUrl;
}
