package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Denydomain generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "denydomain", schema = "public")
public class Denydomain implements java.io.Serializable {

	private long ddId;
	private String DDomain;

	public Denydomain() {
	}

	public Denydomain(long ddId) {
		this.ddId = ddId;
	}

	public Denydomain(long ddId, String DDomain) {
		this.ddId = ddId;
		this.DDomain = DDomain;
	}

	@SequenceGenerator(name="denydomain_seq", sequenceName="denydomain_denydomain_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="denydomain_seq")	@Id
	@Column(name = "dd_id", unique = true, nullable = false)
	public long getDdId() {
		return this.ddId;
	}

	public void setDdId(long ddId) {
		this.ddId = ddId;
	}

	@Column(name = "d_domain", length = 128)
	public String getDDomain() {
		return this.DDomain;
	}

	public void setDDomain(String DDomain) {
		this.DDomain = DDomain;
	}

}
