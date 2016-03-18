package lab.s2jh.module.wxplatform.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.module.auth.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
/**
 * 公众号信息
 * @author wuyongwen
 * @Date 2016年3月17日下午5:43:30
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_mpapp", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "公众号信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxMpApp extends BaseNativeEntity{
	private static final long serialVersionUID = -9031242080677238601L;
	@MetaData(value = "公众号Id")
	@Column(length = 100, nullable = false)
	private String authorizerAppid;
	
	@MetaData(value = "公众号Token")
	@Column(length = 200, nullable = false)
	private String authorizerAccessToken;
	
	@MetaData(value = "公众号失效时间")
	@Column(length = 100, nullable = false)
	private Long expiresOut;
	
	@MetaData(value = "公众号Token刷新码")
	@Column(length = 100, nullable = false)
	private String authorizerRefreshToken;
	
	@MetaData(value = "公众号权限")
	@Column(length = 100)
	private String funcInfo;
	
	@MetaData(value = "昵称")
	@Column(length = 200)
	private String nickName;
	
	@MetaData(value = "头像")
	@Column(length = 400)
	private String headImg;
	
	@MetaData(value = "公众号类型",comments="授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号")
	@Column(length = 100)
	private String serviceTypeInfo;
	
	@MetaData(value = "认证类型",comments="-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，"
			+ "4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证")
	@Column(length = 100)
	private String verifyTypeInfo;
	
	@MetaData(value = "公众号原始ID")
	@Column(length = 100)
	private String userName;
	// 保存格式 {"open_store": 0, "open_scan": 0, "open_pay": 0, "open_card": 0, "open_shake": 0}
	@MetaData(value = "功能开通权限" ,comments=" 用以了解以下功能的开通状况（0代表未开通，1代表已开通）："
			+ "open_store:是否开通微信门店功能;open_scan:是否开通微信扫商品功能;"
			+ "open_pay:是否开通微信支付功能;open_card:是否开通微信卡券功能;open_shake:是否开通微信摇一摇功能")
	@Column(length = 300)
	private String businessInfo;
	
	@Column(length = 100)
	private String openShake;
	
	@MetaData(value = "微信号",comments="授权方公众号所设置的微信号，可能为空")
	@Column(length = 100)
	private String alias;
	
	@MetaData(value = "二维码图片")
	@Column(length = 400)
	private String qrcodeUrl;
	
	@MetaData(value = "登录账号对象")  
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
	private User user;
}
