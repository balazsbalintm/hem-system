package hu.bme.analytics.hems.ui.rapidminer;

import hu.bme.analytics.hems.entities.PersonDistanceResult;

import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CandidateSearchService extends Service<List<PersonDistanceResult>> {
	private String candidateSearchText;
	private InternalExternalSelector intExtSel;
	
	public CandidateSearchService(String candidateSearchText, InternalExternalSelector intExtSel) {
		this.candidateSearchText = candidateSearchText;
		this.intExtSel = intExtSel;
	}
	
	@Override
	protected Task<List<PersonDistanceResult>> createTask() {
		return new Task<List<PersonDistanceResult>>() {
			@Override
			protected List<PersonDistanceResult> call() throws Exception {
				return ModelCaller.executeCandidateSearchModel( candidateSearchText, intExtSel );
			}
		};
	}

}
