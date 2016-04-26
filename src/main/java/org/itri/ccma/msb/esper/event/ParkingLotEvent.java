package org.itri.ccma.msb.esper.event;


public class ParkingLotEvent {
	
	private long id;
	private String parkingLotName;
	private double longitude;
	private double latitude;
	private int avalableForRent;
	private int faultRepairNum;
	private int totalSpace;
	private String telNumber;	
	
	public ParkingLotEvent() {
	}	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getParkingLotName() {
		return parkingLotName;
	}
	public void setParkingLotName(String parkingLotName) {
		this.parkingLotName = parkingLotName;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getAvalableForRent() {
		return avalableForRent;
	}
	public void setAvalableForRent(int avalableForRent) {
		this.avalableForRent = avalableForRent;
	}
	public int getFaultRepairNum() {
		return faultRepairNum;
	}
	public void setFaultRepairNum(int faultRepairNum) {
		this.faultRepairNum = faultRepairNum;
	}
	public int getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}
	public String getTelNumber() {
		return telNumber;
	}
	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

}
