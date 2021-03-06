package org.itri.ccma.tarsan.hibernate;

// Generated 2016/3/21 �U�� 01:28:50 by Hibernate Tools 3.4.0.CR1
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Location generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "location", schema = "public")
public class Location implements java.io.Serializable {

	private long locId;
	private Vacantad vacantad;
	private String house;
	private String floor;
	private String zone;
	private Set <Buyad2loc>buyad2locs = new HashSet<Buyad2loc>(0);

	public Location() {
	}

	public Location(long locId) {
		this.locId = locId;
	}

	public Location(long locId, Vacantad vacantad, String house, String floor,
			String zone, Set <Buyad2loc>buyad2locs) {
		this.locId = locId;
		this.vacantad = vacantad;
		this.house = house;
		this.floor = floor;
		this.zone = zone;
		this.buyad2locs = buyad2locs;
	}

	@SequenceGenerator(name="location_seq", sequenceName="location_location_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="location_seq")	@Id
	@Column(name = "loc_id", unique = true, nullable = false)
	public long getLocId() {
		return this.locId;
	}

	public void setLocId(long locId) {
		this.locId = locId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vacant_id")
	public Vacantad getVacantad() {
		return this.vacantad;
	}

	public void setVacantad(Vacantad vacantad) {
		this.vacantad = vacantad;
	}

	@Column(name = "house", length = 128)
	public String getHouse() {
		return this.house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	@Column(name = "floor", length = 10)
	public String getFloor() {
		return this.floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	@Column(name = "zone", length = 128)
	public String getZone() {
		return this.zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	public Set <Buyad2loc>getBuyad2locs() {
		return this.buyad2locs;
	}

	public void setBuyad2locs(Set <Buyad2loc>buyad2locs) {
		this.buyad2locs = buyad2locs;
	}

}
