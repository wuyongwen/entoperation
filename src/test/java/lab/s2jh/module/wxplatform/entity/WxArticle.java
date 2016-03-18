package lab.s2jh.module.wxplatform.entity;

import java.sql.Clob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * 微信稿件
 * @author wuyongwen
 * @Date 2016年3月18日下午3:43:45
 */

@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_mpapp", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "稿件")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxArticle extends BaseNativeEntity {
	private static final long serialVersionUID = 5839773369514370950L;
	@MetaData(value = "创建者")
	@Column(length = 100, nullable = false)
	private String creator;
	@MetaData(value = "标题")
	@Column(length = 300, nullable = false)
	private String title;
	@MetaData(value = "封面素材ID")
	@Column(length = 200)
	private String thumbMediaId;
	@MetaData(value = "封面URL")
	@Column(length = 200, nullable = false)
	private String thumbUrl;
	
	@MetaData(value = "显示封面")
	@Column(nullable = false)
	private Integer showCoverPic;
	@MetaData(value = "作者")
	@Column(length=100, nullable = false)
	private String auth;
	@MetaData(value = "摘要")
	@Column(length=400)
	private String digest;
	@MetaData(value = "正文")
	@Column(nullable = false)
	private Clob content;
	@MetaData(value = "原文地址")
	@Column(length=200)
	private String contentSourceUrl;
	@MetaData(value = "更新时间")
	@Column(nullable = false)
	private Date updateTime;
	
	@MetaData("稿件组ID")
	@ManyToOne
	@JoinColumn(name="group_id",nullable=false)
	private WxArticleGroup group;
	
	@MetaData("引用图片")
	@OneToMany(mappedBy="",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<WxReferenceImg> images = new ArrayList<WxReferenceImg>();
}
