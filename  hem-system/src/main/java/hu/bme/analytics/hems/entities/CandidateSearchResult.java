package hu.bme.analytics.hems.entities;

import java.util.Date;

public class CandidateSearchResult implements Comparable<CandidateSearchResult> {
	private Employee emp;
	private String searchKeyword;
	private Date searchDate;
	private double distance;
	
	public CandidateSearchResult() {}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	@Override
	public int compareTo(CandidateSearchResult csr) {
		if(csr != null) {
			if(distance < csr.getDistance()) {
				return -1;
			} else if (distance > csr.getDistance()) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
}
