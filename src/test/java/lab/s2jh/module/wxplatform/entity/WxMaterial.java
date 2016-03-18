package lab.s2jh.module.wxplatform.entity;

import java.sql.Date;

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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 微信素材库
 * 
 * @author wuyongwen
 * @Date 2016年3月18日上午11:36:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_mpapp", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "素材库")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxMaterial extends BaseNativeEntity {
	private static final long serialVersionUID = 8846732066281646963L;
	@MetaData(value = "素材ID")
	@Column(length = 200, nullable = false)
	private String mediaId;

	@MetaData(value = "名称")
	@Column(length = 100, nullable = false)
	private String name;

	@MetaData(value = "创建时间")
	@Column
	private Date updateTime;

	@MetaData(value = "素材URL")
	@Column(length = 400, nullable = false)
	private String url;

	@MetaData(value = "类型", comments = "素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）")
	@Column(nullable = false)
	private Integer type;

	@MetaData(value="appId")
	@ManyToOne
	@JoinColumn(name = "app_id", nullable = false)
	private WxMpApp app;
}
