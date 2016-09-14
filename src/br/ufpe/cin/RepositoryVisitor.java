package br.ufpe.cin;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import br.com.metricminer2.domain.Commit;
import br.com.metricminer2.parser.jdt.JDTRunner;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.scm.CommitVisitor;
import br.com.metricminer2.scm.RepositoryFile;
import br.com.metricminer2.scm.SCMRepository;

public class RepositoryVisitor implements CommitVisitor {

	@Override
	public String name() {
		return "Repositories";
	}

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		System.out.println("Total Commits: " + repo.getScm().totalCommits());
		System.out.println("Files: " + repo.getScm().files().size());
		
		System.out.println("Names: " + repo.getScm().files().toString());
		
		for(RepositoryFile f : repo.getScm().files()) {
			if(!f.fileNameEndsWith("java") && !f.getFile().getName().contains("AndroidManifest") && !f.getFullName().contains("res\\layout")) continue;
			
			ClassesVisitor test = new ClassesVisitor(f);
			//new JDTRunner().visit(test, new ByteArrayInputStream(f.getSourceCode().getBytes()));
			
			test.visit();
			int methods = test.getQty();
			ArrayList<String> names = test.getNames();
			String temp = "";
			
			for (int i = 0; i < names.size(); i++) {
				if(i != 0) temp = names.get(i);
				else temp = temp + ", " + names.get(i);
			}
			
			writer.write(f.getFullName(), methods,temp);
		}
		
	}

}
