package hu.bme.analytics.hems.ui.rapidminer;

import hu.bme.analytics.hems.entities.PersonDistanceResult;

import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CandidateSearchService extends Service<List<PersonDistanceResult>> {
	private String candidateSearchText;
	
	public CandidateSearchService(String candidateSearchText) {
		this.candidateSearchText = candidateSearchText;
	}
	
	@Override
	protected Task<List<PersonDistanceResult>> createTask() {
		return new Task<List<PersonDistanceResult>>() {
			@Override
			protected List<PersonDistanceResult> call() throws Exception {
				return ModelCaller.executeCandidateSearchModel( candidateSearchText );
			}
		};
	}

}
