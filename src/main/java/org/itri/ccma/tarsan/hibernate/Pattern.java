package org.itri.ccma.tarsan.hibernate;
// Generated Sep 3, 2015 11:03:17 AM by Hibernate Tools 4.3.1
import com.fasterxml.jackson.annotation.JsonIgnore;import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Pattern generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "pattern")
public class Pattern implements java.io.Serializable {

	private long patternId;
	private String urlHost;
	private String urlPath;
	private String signature;
	private Integer patternType;
	private Integer webType;
	private Date createdDateTime;
	private Date lastAccess;
	private boolean expiredFlag;
	private boolean deletedFlag;
	private boolean confirmedFlag;
	private Set<Pattern2ad> pattern2ads = new HashSet<Pattern2ad>(0);
	private Set<Userevent> userevents = new HashSet<Userevent>(0);

	public Pattern() {
	}

	public Pattern(long patternId, String urlHost, Date createdDateTime, boolean expiredFlag, boolean deletedFlag,
			boolean confirmedFlag) {
		this.patternId = patternId;
		this.urlHost = urlHost;
		this.createdDateTime = createdDateTime;
		this.expiredFlag = expiredFlag;
		this.deletedFlag = deletedFlag;
		this.confirmedFlag = confirmedFlag;
	}

	public Pattern(long patternId, String urlHost, String urlPath, String signature, Integer patternType,
			Integer webType, Date createdDateTime, Date lastAccess, boolean expiredFlag, boolean deletedFlag,
			boolean confirmedFlag, Set<Pattern2ad> pattern2ads, Set<Userevent> userevents) {
		this.patternId = patternId;
		this.urlHost = urlHost;
		this.urlPath = urlPath;
		this.signature = signature;
		this.patternType = patternType;
		this.webType = webType;
		this.createdDateTime = createdDateTime;
		this.lastAccess = lastAccess;
		this.expiredFlag = expiredFlag;
		this.deletedFlag = deletedFlag;
		this.confirmedFlag = confirmedFlag;
		this.pattern2ads = pattern2ads;
		this.userevents = userevents;
	}

	@SequenceGenerator(name="pattern_seq", sequenceName="pattern_pattern_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pattern_seq")	@Id

	@Column(name = "pattern_id", unique = true, nullable = false)
	public long getPatternId() {
		return this.patternId;
	}

	public void setPatternId(long patternId) {
		this.patternId = patternId;
	}

	@Column(name = "url_host", nullable = false, length = 128)
	public String getUrlHost() {
		return this.urlHost;
	}

	public void setUrlHost(String urlHost) {
		this.urlHost = urlHost;
	}

	@Column(name = "url_path", length = 2048)
	public String getUrlPath() {
		return this.urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	@Column(name = "signature", length = 128)
	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Column(name = "pattern_type")
	public Integer getPatternType() {
		return this.patternType;
	}

	public void setPatternType(Integer patternType) {
		this.patternType = patternType;
	}

	@Column(name = "web_type")
	public Integer getWebType() {
		return this.webType;
	}

	public void setWebType(Integer webType) {
		this.webType = webType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date_time", nullable = false, length = 29)
	public Date getCreatedDateTime() {
		return this.createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_access", length = 29)
	public Date getLastAccess() {
		return this.lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	@Column(name = "expired_flag", nullable = false)
	public boolean isExpiredFlag() {
		return this.expiredFlag;
	}

	public void setExpiredFlag(boolean expiredFlag) {
		this.expiredFlag = expiredFlag;
	}

	@Column(name = "deleted_flag", nullable = false)
	public boolean isDeletedFlag() {
		return this.deletedFlag;
	}

	public void setDeletedFlag(boolean deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	@Column(name = "confirmed_flag", nullable = false)
	public boolean isConfirmedFlag() {
		return this.confirmedFlag;
	}

	public void setConfirmedFlag(boolean confirmedFlag) {
		this.confirmedFlag = confirmedFlag;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pattern")
	@JsonIgnore	public Set<Pattern2ad> getPattern2ads() {
		return this.pattern2ads;
	}

	public void setPattern2ads(Set<Pattern2ad> pattern2ads) {
		this.pattern2ads = pattern2ads;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pattern")
	@JsonIgnore	public Set<Userevent> getUserevents() {
		return this.userevents;
	}

	public void setUserevents(Set<Userevent> userevents) {
		this.userevents = userevents;
	}

}
