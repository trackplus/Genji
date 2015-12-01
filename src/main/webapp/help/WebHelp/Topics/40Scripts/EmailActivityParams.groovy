
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.aurel.track.fieldType.constants.SystemFields;
import com.aurel.track.util.emailHandling.EmailCleanUtils;


public class EmailActivityParams {

	private EmailActivityParams() {
	Map<String, List<String>> subjectStringToFields = new HashMap<String, List<String>>();
	/************************Change here************************************************/

	/**
	 * EXAMPLE:
	 * subjectStringToFields.put("MatchPattern", ["Project", "Subsystem", "Manager", "Responsible"]);
	 **/

	subjectStringToFields.put("D4 Recorder", ["MDR", "MDR D4 Recorder", "Guhl,Michael", "Tamalun,Christoph"]);
	subjectStringToFields.put("Manual", ["MDR", "MDR Manual", "Guhl,Michael", "Liepold,Joachim"]);
	subjectStringToFields.put("MAMU", ["MDR", "MAMU", "Guhl,Michael", "Faber,Marc"]);
	subjectStringToFields.put("MARR", ["MDR", "MARR16", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("MDR F", ["MDR", "MDR Firmware", "Guhl,Michael", "Galunic,Goran"]);
	subjectStringToFields.put("MDR General", ["MDR", "MDR General", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("MDR Hardware", ["MDR", "MDR Hardware", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("METH", ["MDR", "METH2", "Guhl,Michael", "Albers,Marcel"]);
	subjectStringToFields.put("MMRG", ["MDR", "MMRG8", "Guhl,Michael", "Faber,Marc"]);
	subjectStringToFields.put("MUAR", ["MDR", "MUAR6", "Guhl,Michael", "Faber,Marc"]);
	subjectStringToFields.put("MUSM", ["MDR", "MUSM16", "Guhl,Michael", "Juranek,Milan"]);
	subjectStringToFields.put("MVCR1", ["MDR", "MVCR1", "Guhl,Michael", "Albers,Marcel"]);
	subjectStringToFields.put("MVCR2", ["MDR", "MVCR2", "Guhl,Michael", "Albers,Marcel"]);
	subjectStringToFields.put("MANA12", ["MDR", "MANA12", "Guhl,Michael", "Juranek,Milan"]);
	subjectStringToFields.put("MVCR3", ["MDR", "MVCR3", "Guhl,Michael", "Kleiner,Andreas"]);
	subjectStringToFields.put("MANA", ["MDR", "MANA8", "Guhl,Michael", "Juranek,Milan"]);
	subjectStringToFields.put("TIME", ["MDR", "TIME", "Guhl,Michael", "Szell,Andras"]);
	subjectStringToFields.put("SM", ["MDR", "RSM / DSM", "Guhl,Michael", "Czupi,Karoly"]);

	subjectStringToFields.put("Cartridges", ["DATaRec 3", "Cartridges", "Bago,Balazs", "Czupi,Karoly"]);
	subjectStringToFields.put("D20", ["DATaRec 3", "D200 Series", "Bago,Balazs", "Bago,Balazs"]);
	subjectStringToFields.put("D500", ["DATaRec 3", "D5000 Series", "Bago,Balazs", "Bago,Balazs"]);
	subjectStringToFields.put("D7000 Series", ["DATaRec 3", "D7000 Series", "Bago,Balazs", "Bago,Balazs"]);
	subjectStringToFields.put("DATaRec 3 General", ["DATaRec 3", "General", "Bago,Balazs", "Bago,Balazs"]);
	subjectStringToFields.put("DATaRec 3 Modules", ["DATaRec 3", "Modules", "Bago,Balazs", "Ratz,Csaba"]);
	subjectStringToFields.put("Manua", ["DATaRec 3", "Manual", "Bago,Balazs", "Liepold,Joachim"]);
	subjectStringToFields.put("Firmware", ["DATaRec 3", "Firmware", "Bago,Balazs", "Bago,Balazs"]);
	subjectStringToFields.put("GSR", ["DATaRec 3", "GSR Series", "Bago,Balazs", "Czupi,Karoly"]);

	subjectStringToFields.put("CAN4", ["DATaRec 4", "CAN4", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("CHG6", ["DATaRec 4", "CHG6", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("D4 Recorder", ["DATaRec 4", "D4 Recorder", "Guhl,Michael", "Tamalun,Christoph"]);
	subjectStringToFields.put("DATaRec 4 Software", ["DATaRec 4", "Software", "Guhl,Michael", "Marin,Cruz"]);
	subjectStringToFields.put("DATaRec 4 Storage Module", ["DATaRec 4", "Storage Module", "Guhl,Michael", "Czupi,Karoly"]);
	subjectStringToFields.put("DATaRec 4 General", ["DATaRec 4", "General", "Guhl,Michael", "Marin,Cruz"]);
	subjectStringToFields.put("DEBU", ["DATaRec 4", "DEBU", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("DIC24", ["DATaRec 4", "DIC24", "Guhl,Michael", "Kleiner,Andreas"]);
	subjectStringToFields.put("DIC6", ["DATaRec 4", "DIC6", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("LMF", ["DATaRec 4", "Link Module", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("MIC6", ["DATaRec 4", "MIC6", "Guhl,Michael", "Lencses,Zoltan"]);
	subjectStringToFields.put("OUT6", ["DATaRec 4", "OUT6", "Guhl,Michael", "Ratz,Csaba"]);
	subjectStringToFields.put("Power Modules", ["DATaRec 4", "Power Modules", "Guhl,Michael", "Czupi,Karoly"]);
	subjectStringToFields.put("SGU", ["DATaRec 4", "SGU9", "Guhl,Michael", "Juranek,Milan"]);
	subjectStringToFields.put("FIRMW", ["DATaRec 4", "Firmware", "Guhl,Michael", "Handke,Stephan"]);
	subjectStringToFields.put("Manu", ["DATaRec 4", "Manual", "Guhl,Michael", "Liepold,Joachim"]);

	subjectStringToFields.put("ANH1", ["GSSr + Modules", "ANH100 / ANH101", "Handke,Stephan", "Kleiner,Andreas"]);
	subjectStringToFields.put("ARP1", ["GSSr + Modules", "ARP100 / ARR100", "Handke,Stephan", "Ratz,Csaba"]);
	subjectStringToFields.put("ARR1", ["GSSr + Modules", "ARP100 / ARR100", "Handke,Stephan", "Ratz,Csaba"]);
	subjectStringToFields.put("ASM1", ["GSSr + Modules", "ASM100", "Handke,Stephan", "Juranek,Milan"]);
	subjectStringToFields.put("ETH1", ["GSSr + Modules", "ETH100", "Handke,Stephan", "Albers, Marcel"]);
	subjectStringToFields.put("GSS", ["GSSr + Modules", "GSSr PC", "Handke,Stephan", "Szalai,Zoltan"]);
	subjectStringToFields.put("GSSr \\+ ModulesGeneral", ["GSSr + Modules", "General", "Handke,Stephan", "Handke,Stephan"]);
	subjectStringToFields.put("LMF1G", ["GSSr + Modules", "Link Module", "Handke,Stephan", "Ratz,Csaba"]);
	subjectStringToFields.put("MRG1", ["GSSr + Modules", "MRG100", "Handke,Stephan", "Faber,Marc"]);
	subjectStringToFields.put("Server", ["GSSr + Modules", "Server", "Handke,Stephan", "Szalai,Zoltan"]);
	subjectStringToFields.put("UAP1", ["GSSr + Modules", "UAP100 / UAR100", "Handke,Stephan", "Faber,Marc"]);
	subjectStringToFields.put("UAR1", ["GSSr + Modules", "UAP100 / UAR100", "Handke,Stephan", "Faber,Marc"]);
	subjectStringToFields.put("VCP1", ["GSSr + Modules", "VCP100 / VCR100", "Handke,Stephan", "Albers,Marcel"]);
	subjectStringToFields.put("VCR10", ["GSSr + Modules", "VCP100 / VCR100", "Handke,Stephan", "Albers,Marcel"]);
	subjectStringToFields.put("PicoGSS", ["GSSr + Modules", "PicoGSS", "Handke,Stephan", "Zsalai,Zoltan"]);
	subjectStringToFields.put("FIRMWA", ["GSSr + Modules", "Firmware", "Roehrig,Roger", "Handke,Stephan"]);
	subjectStringToFields.put("DataShark", ["GSSr + Modules", "DataShark", "Handke,Stephan", "Zsalai,Zoltan"]);
	subjectStringToFields.put("Manual G", ["GSSr + Modules", "Manual", "Guhl,Michael", "Liepold,Joachim"]);

	subjectStringToFields.put("NS", ["Non Standard Request", "USA", "Guhl,Michael", "Marin,Cruz"]);
	subjectStringToFields.put("DNS", ["Non Standard Request", "ROW", "Guhl,Michael", "Marin,Cruz"]);

	subjectStringToFields.put("Asia", ["sales", "Asia", "Isermann,Wolfgang", "Frank,Hein"]);
	subjectStringToFields.put("Europe", ["sales", "Europe", "Isermann,Wolfgang", "Morel,Pascal"]);
	subjectStringToFields.put("Germany", ["sales", "Germany", "Isermann,Wolfgang", "Ingenrieth,Josef"]);
	subjectStringToFields.put("support", ["sales", "Sales support", "Isermann,Wolfgang", "Marin,Cruz"]);
	subjectStringToFields.put("USA", ["sales", "USA", "Blaak,Olaf", "Isermann,Wolfgang"]);
	subjectStringToFields.put("AAAD", ["sales", "Admin", "Isermann,Wolfgang", "Foerster,Nicole"]);

	subjectStringToFields.put("QS", ["QM / Test", "Quality Issue Customer", "Guhl,Michael", "Dolf,Dieter"]);
	subjectStringToFields.put("QS1", ["QM / Test", "ATE Issue Testing", "Dolf,Dieter", "Truschies,Michael"]);
	subjectStringToFields.put("QS2", ["QM / Test", "ATE Test Planning", "Dolf,Dieter", "Truschies,Michael"]);
	subjectStringToFields.put("QS3", ["QM / Test", "ATE Test Report", "Dolf,Dieter", "Truschies,Michael"]);
	subjectStringToFields.put("QS4", ["QM / Test", "ENV Issue Testing", "Dolf,Dieter", "Kropat,Holger"]);
	subjectStringToFields.put("QS5", ["QM / Test", "ENV Test Planning", "Dolf,Dieter", "Kropat,Holger"]);
	subjectStringToFields.put("QS6", ["QM / Test", "ENV Test Reports", "Dolf,Dieter", "Kropat,Holger"]);
	subjectStringToFields.put("QS7", ["QM / Test", "Quality Issue PCB", "Dolf,Dieter", "Schulz,Michael"]);
	subjectStringToFields.put("QS8", ["QM / Test", "Quality Issue Supplier", "Dolf,Dieter", "Schulz,Michael"]);
	subjectStringToFields.put("QS9", ["QM / Test", "SYS Issue Testing", "Dolf,Dieter", "Stember,Robin"]);
	subjectStringToFields.put("QS10", ["QM / Test", "SYS Test Planning", "Dolf,Dieter", "Stember,Robin"]);
	subjectStringToFields.put("QS11", ["QM / Test", "SYS Test Report", "Dolf,Dieter", "Stember,Robin"]);

	/***************************Reject attachments with name***************************************/
	List<String> rejectAttachments = new LinkedList<String>();
	rejectAttachments.add("pic\\d.*\\.jpg");
	rejectAttachments.add("\\d.*\\.jpg");
	rejectAttachments.add(".*graycol.*\\.gif");
	rejectAttachments.add(".*binaryPart.*?\\.dat");
	rejectAttachments.add("*binaryPart.*\\.dat");
	rejectAttachments.add(".*binaryPart\\.dat");
	rejectAttachments.add(".*?\\.vcf");
	
	/***************************Cut expressions***************************************/
	List<String> cutStrings = new LinkedList<String>();
	//the regEx which if found in email description will be removed
	cutStrings.add("ZODIAC\\s*DATA\\s*SYSTEMS.*?HRB\\s*47131");
	cutStrings.add("ZODIAC\\s*DATA\\s*SYSTEMS.*?NO\\s*EXPORT\\s*IS\\s*AUTHORIZED\\s*HEREBY");
	cutStrings.add("This e-mail may contain confidential.*?We thank you for your cooperation\\.");
	
	/**************************Do not modify after this line ***************************************/
	
	rejectAttachmnentPatterns = EmailCleanUtils.compilePatterns(rejectAttachments);
	cutPatterns = EmailCleanUtils.compilePatterns(cutStrings, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
	patternToFields = new HashMap<Pattern, List<String>>();
	projectsToIssueFields = new HashMap<String, Map<String, Map<Integer, String>>>();
	for (String subjectMatcherString : subjectStringToFields.keySet()) {
		try {
			Pattern pattern = Pattern.compile(subjectMatcherString);
			if (pattern!=null) {
				patternToFields.put(pattern, subjectStringToFields.get(subjectMatcherString));
			}
		} catch (Exception e) {
			System.out.println("The subject matcher string regex " + subjectMatcherString + " can't be compiled " + e.getMessage());
		}
	}
	for (List<String> issueFields : subjectStringToFields.values()) {
		String projectLabel = issueFields.get(0);
		if (projectLabel!=null) {
			Map<String, Map<Integer, String>> subprojectToIssueFields = (Map<String, Map<Integer, String>>)projectsToIssueFields.get(projectLabel);
			if (subprojectToIssueFields==null) {
				subprojectToIssueFields = new HashMap<String, Map<Integer, String>>();
				projectsToIssueFields.put(projectLabel, subprojectToIssueFields);
			}
			String subsystemLabel = issueFields.get(1);
			if (subsystemLabel!=null) {
				Map<Integer, String> issueFieldsMap = new HashMap<Integer, String>();
				issueFieldsMap.put(SystemFields.MANAGER, issueFields.get(2));
				issueFieldsMap.put(SystemFields.RESPONSIBLE, issueFields.get(3));
				subprojectToIssueFields.put(subsystemLabel, issueFieldsMap);
			}
		}
	}
	}
	
	private static Map<String, Map<String, Map<Integer, String>>> projectsToIssueFields;
	private static EmailActivityParams instance = null;
	private static Map<Pattern, List<String>> patternToFields;
	private static List<Pattern> rejectAttachmnentPatterns;
	private static List<Pattern> cutPatterns;
	
	public static EmailActivityParams getInstance() {
		if (instance==null) {
			instance = new EmailActivityParams();
		}
		return instance;
	}

	public static Map<Pattern, List<String>> getPatternToFields() {
		return patternToFields;
	}
   
	public static List<Pattern> getCutPatterns() {
		return cutPatterns;
	}
	
	public static List<Pattern> getRejectAttachmnentPatterns() {
		return rejectAttachmnentPatterns;
	}
	
	public static Map<String, Map<String, Map<Integer, String>>> getProjectsToIssueFields() {
		return projectsToIssueFields;
	}
}
