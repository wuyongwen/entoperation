package lab.s2jh.module.wxplatform.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@MetaData(value = "图文消息列表")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxArticleGroup extends BaseNativeEntity {
	private static final long serialVersionUID = -5674613585135699947L;
	@MetaData(value = "创建者")
	@Column(length = 100, nullable = false)
	private String creator;
	@MetaData(value = "状态")
	@Column(nullable = false)
	private Integer status;
	@MetaData(value = "素材ID")
	@Column(length = 200, nullable = false)
	private String mediaId;
	@MetaData(value = "是否群发")
	@Column
	private Boolean isToAll;
	@MetaData(value = "消息类型")
	@Column(length = 100, nullable = false)
	private String msgtype;
	
	@MetaData("稿件列表")
	@OneToMany(mappedBy="group",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	List<WxArticle> artcles = new ArrayList<WxArticle>();
}
