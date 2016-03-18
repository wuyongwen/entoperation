package lab.s2jh.module.wxplatform.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
 * 图片
 * @author wuyongwen
 * @Date 2016年3月18日下午5:53:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Entity
@Table(name = "wx_mpapp", uniqueConstraints = @UniqueConstraint(columnNames = { "id" }) )
@MetaData(value = "稿件")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WxReferenceImg extends BaseNativeEntity {
	private static final long serialVersionUID = -7754100098991054409L;
	@MetaData(value = "图片类型")
	@Column(length = 200, nullable = false)
	private String type;
	@MetaData(value = "名称")
	@Column(length = 100, nullable = false)
	private String name;
	@MetaData(value = "图片本地地址")
	@Column(length = 200)
	private String thumbLocal;
	@MetaData(value = "图片地址")
	@Column(length = 200, nullable = false)
	private String remotePath;
	@MetaData(value = "图片格式")
	@Column(length = 100, nullable = false)
	private String contentType;
	
	@MetaData("创建者")
	@Column(length=100 ,nullable = false)
	private String userCode;
	@MetaData("稿件ID")
	@OneToMany
	@JoinColumn(name = "article_id")
	private WxArticle article;
}
