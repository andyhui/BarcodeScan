package com.http.service;

import java.util.List;

public class TestResult {
	private String status;
	private List<Result> results;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return "User [status=" + status + ", results=" + results + "]";
	}

}
