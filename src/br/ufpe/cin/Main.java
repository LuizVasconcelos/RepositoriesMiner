package br.ufpe.cin;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.persistence.csv.CSVFile;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class Main implements Study {
	
	private String[] directories = new String[2];
	
	public Main() {
		directories[0] = "C:\\Users\\Luiz Vasconcelos\\work\\RepositoriesMiner\\repos\\Androzic\\Androzic";
		directories[1] = "C:\\Users\\Luiz Vasconcelos\\work\\RepositoriesMiner\\repos\\ATT\\AAT";
		//directories[2] = "C:\\Users\\Luiz Vasconcelos\\work\\RepositoriesMiner\\repos\\BRouter\\brouter\\brouter-routing-app";
	}
	
	@Override
	public void execute() {
		for (int i = 0; i < directories.length; i++) {
			new RepositoryMining().
			in(GitRepository.singleProject(this.directories[i])).
			through(Commits.onlyInHead()).
			process(new RepositoryVisitor(), new CSVFile("C:\\Users\\Luiz Vasconcelos\\work\\RepositoriesMiner\\test" + i + ".csv")).
			mine();
		}
		
	}
	
	
	public static void main(String[] args) {
		new MetricMiner2().start(new Main());
	}
}
