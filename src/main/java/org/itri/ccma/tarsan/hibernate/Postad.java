package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Postad generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "postad", schema = "public")
public class Postad implements java.io.Serializable {

	private PostadId id;
	private Buyad buyad;
	private String showtimes;
	private String clicktimes;
	private Boolean close;
	private Date createdate;
	private Date updatetime;
	private Set <Logad>logads = new HashSet<Logad>(0);

	public Postad() {
	}

	public Postad(PostadId id, Buyad buyad, Date createdate) {
		this.id = id;
		this.buyad = buyad;
		this.createdate = createdate;
	}

	public Postad(PostadId id, Buyad buyad, String showtimes,
			String clicktimes, Boolean close, Date createdate, Date updatetime,
			Set <Logad>logads) {
		this.id = id;
		this.buyad = buyad;
		this.showtimes = showtimes;
		this.clicktimes = clicktimes;
		this.close = close;
		this.createdate = createdate;
		this.updatetime = updatetime;
		this.logads = logads;
	}

	@SequenceGenerator(name="postad_seq", sequenceName="postad_postad_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="postad_seq")	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "postadId", column = @Column(name = "postad_id", nullable = false)),
			@AttributeOverride(name = "buyadId", column = @Column(name = "buyad_id", nullable = false)) })
	public PostadId getId() {
		return this.id;
	}

	public void setId(PostadId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyad_id", nullable = false, insertable = false, updatable = false)
	public Buyad getBuyad() {
		return this.buyad;
	}

	public void setBuyad(Buyad buyad) {
		this.buyad = buyad;
	}

	@Column(name = "showtimes", length = 128)
	public String getShowtimes() {
		return this.showtimes;
	}

	public void setShowtimes(String showtimes) {
		this.showtimes = showtimes;
	}

	@Column(name = "clicktimes", length = 128)
	public String getClicktimes() {
		return this.clicktimes;
	}

	public void setClicktimes(String clicktimes) {
		this.clicktimes = clicktimes;
	}

	@Column(name = "close")
	public Boolean getClose() {
		return this.close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", nullable = false, length = 29)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "postad")
	public Set <Logad>getLogads() {
		return this.logads;
	}

	public void setLogads(Set <Logad>logads) {
		this.logads = logads;
	}

}
