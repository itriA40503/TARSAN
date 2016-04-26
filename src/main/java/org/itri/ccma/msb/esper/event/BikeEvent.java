package org.itri.ccma.msb.esper.event;

public class BikeEvent
{

	String id;
	Double longitude;
	Double latitude;

	public BikeEvent(String id, Double longitude, Double latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "id: " + id + " ,longitude: " + longitude + " ,latitude: " + latitude;
	}
}
