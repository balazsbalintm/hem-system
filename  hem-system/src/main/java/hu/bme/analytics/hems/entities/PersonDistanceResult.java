package hu.bme.analytics.hems.entities;

public class PersonDistanceResult implements Comparable<PersonDistanceResult>{
	private double personId;
	private double distance;
	
	public PersonDistanceResult(double personId, double distance) {
		this.personId = personId;
		this.distance = distance;
	}

	public double getPersonId() {
		return personId;
	}

	public void setPersonId(double personId) {
		this.personId = personId;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(PersonDistanceResult o) {
		if(o != null) {
			if(distance > o.getDistance()) {
				return 1;
			} else if (distance < o.getDistance()) {
				return -1;
			} else {
				return 0;
			}
		}
		
		return 0;
	}
}