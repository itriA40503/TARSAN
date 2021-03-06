package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Budgetlog generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "budgetlog", schema = "public")
public class Budgetlog implements java.io.Serializable {

	private long budgetlogId;
	private Budgetpool budgetpool;
	private Long budgetTotal;
	private String counting;
	private Date createdate;

	public Budgetlog() {
	}

	public Budgetlog(long budgetlogId) {
		this.budgetlogId = budgetlogId;
	}

	public Budgetlog(long budgetlogId, Budgetpool budgetpool, Long budgetTotal,
			String counting, Date createdate) {
		this.budgetlogId = budgetlogId;
		this.budgetpool = budgetpool;
		this.budgetTotal = budgetTotal;
		this.counting = counting;
		this.createdate = createdate;
	}

	@SequenceGenerator(name="budgetlog_seq", sequenceName="budgetlog_budgetlog_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="budgetlog_seq")	@Id
	@Column(name = "budgetlog_id", unique = true, nullable = false)
	public long getBudgetlogId() {
		return this.budgetlogId;
	}

	public void setBudgetlogId(long budgetlogId) {
		this.budgetlogId = budgetlogId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "budget_id")
	public Budgetpool getBudgetpool() {
		return this.budgetpool;
	}

	public void setBudgetpool(Budgetpool budgetpool) {
		this.budgetpool = budgetpool;
	}

	@Column(name = "budget_total")
	public Long getBudgetTotal() {
		return this.budgetTotal;
	}

	public void setBudgetTotal(Long budgetTotal) {
		this.budgetTotal = budgetTotal;
	}

	@Column(name = "counting", length = 128)
	public String getCounting() {
		return this.counting;
	}

	public void setCounting(String counting) {
		this.counting = counting;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdate", length = 29)
	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

}
