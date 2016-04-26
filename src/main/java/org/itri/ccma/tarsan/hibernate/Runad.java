package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Runad generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "runad", schema = "public")
public class Runad implements java.io.Serializable {

	private RunadId id;
	private String keywords;
	private String type;
	private String position;
	private String size;
	private String content;
	private String location;
	private Date startdate;
	private Date enddate;
	private String intervaltime;
	private Date createdate;
	private Date updatetime;
	private Boolean close;

	public Runad() {
	}

	public Runad(RunadId id) {
		this.id = id;
	}

	public Runad(RunadId id, String keywords, String type, String position,
			String size, String content, String location, Date startdate,
			Date enddate, String intervaltime, Date createdate,
			Date updatetime, Boolean close) {
		this.id = id;
		this.keywords = keywords;
		this.type = type;
		this.position = position;
		this.size = size;
		this.content = content;
		this.location = location;
		this.startdate = startdate;
		this.enddate = enddate;
		this.intervaltime = intervaltime;
		this.createdate = createdate;
		this.updatetime = updatetime;
		this.close = close;
	}

	@SequenceGenerator(name="runad_seq", sequenceName="runad_runad_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="runad_seq")	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "runadId", column = @Column(name = "runad_id", nullable = false)),
			@AttributeOverride(name = "postadId", column = @Column(name = "postad_id", nullable = false)),
			@AttributeOverride(name = "buyadId", column = @Column(name = "buyad_id", nullable = false)) })
	public RunadId getId() {
		return this.id;
	}

	public void setId(RunadId id) {
		this.id = id;
	}

	@Column(name = "keywords", length = 128)
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "type", length = 128)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "position", length = 128)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "size", length = 128)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Column(name = "content", length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "location", length = 128)
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startdate", length = 29)
	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "enddate", length = 29)
	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	@Column(name = "intervaltime", length = 128)
	public String getIntervaltime() {
		return this.intervaltime;
	}

	public void setIntervaltime(String intervaltime) {
		this.intervaltime = intervaltime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", length = 29)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatetime", length = 29)
	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "close")
	public Boolean getClose() {
		return this.close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

}
