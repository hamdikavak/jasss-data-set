package com.hamdikavak.data.retrieval.jasss.test;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hamdikavak.data.retrieval.jasss.IssuePage;
import com.hamdikavak.data.retrieval.jasss.JASSSDataExtracter;

/**
 * 
 * @author Hamdi Kavak
 *	
 * This class tests the number of articles per issue
 */
public class ArticleNumberTester {

	private static final Logger logger = LogManager.getLogger(ArticleNumberTester.class);
	private static ArrayList<IssuePageTest> issuePageTestList = new ArrayList<IssuePageTest>();
	
	public static void main(String[] args) {
		
		(new ArticleNumberTester()).runTest();
	}
	
	public void runTest(){
		logger.info("Test is starting...");
		JASSSDataExtracter jasssDataExtracter = new JASSSDataExtracter();
		ArrayList<String> issueURLs = jasssDataExtracter.extractIssueURLs();
		IssuePage tempIssue;
		IssuePageTest tempIssuePageTest;
		
		issueURLs = jasssDataExtracter.extractIssueURLs();

		for (String issueURL : issueURLs) {
			tempIssue = jasssDataExtracter.extractIssuePageContent(issueURL);
			tempIssuePageTest = getActualNumbers(tempIssue.getVolumeId(), tempIssue.getIssueId());
			logger.info("Volume: "+ tempIssue.getVolumeId() + " - Issue: "+ tempIssue.getIssueId() + " => " + issueURL);
			if(tempIssue.getRefereedArticleURLs().size() != tempIssuePageTest.getNumOfRefereed() ){
				logger.info("Refereed article # actual vs. extracted " + tempIssuePageTest.getNumOfRefereed() + "- " + tempIssue.getRefereedArticleURLs().size() );
			}
			if(tempIssue.getForumArticleURLs().size() != tempIssuePageTest.getNumOfForum()){
				logger.info("Forum article # actual vs. extracted " + tempIssuePageTest.getNumOfForum() + "- " + tempIssue.getForumArticleURLs().size() );
			}
			if(tempIssue.getReviewArticleURLs().size() != tempIssuePageTest.getNumOfReview()){
				logger.info("Review article # actual vs. extracted " + tempIssuePageTest.getNumOfReview() + "- " + tempIssue.getReviewArticleURLs().size() );
			}
		}
	}
	
	public ArticleNumberTester(){
		setNumOfArticles();
	}
	
	public IssuePageTest getActualNumbers(int volumeId, int issueId){
		for(IssuePageTest pg: issuePageTestList){
			if(pg.getVolumeId()== volumeId && pg.getIssueId() == issueId){
				return pg;
			}
		}
		
		return null;
	}
	
	/**
	 * fills the current number of different articles by issue.
	 * the numbers are manually obtained from the JASSS website.
	 */
	private static void setNumOfArticles(){
		issuePageTestList = new ArrayList<IssuePageTest>();
		int volumeId = 1;
		// volume #1
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 3, 3, 1));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 4, 1, 2));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 4, 1, 3));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 3, 2, 3));
		
		volumeId = 2;
		// volume #2
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 2, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 3, 1, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 7, 2, 5));
		
		volumeId = 3;
		// volume #3
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 2, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 2, 2, 0));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 2, 2, 3));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 3, 1, 2));
		
		volumeId = 4;
		// volume #4
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 4, 2, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 11, 1, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 10, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 6, 2, 7));
		
		volumeId = 5;
		// volume #5
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 4, 3, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 4, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 6, 3, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 5, 1, 6));
		
		volumeId = 6;
		// volume #6
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 4, 3, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 8, 2, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 7, 2, 1));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 11, 1, 3));
		
		volumeId = 7;
		// volume #7
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 5, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 9, 0, 1));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 7, 0, 2));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 8, 0, 3));
		
		volumeId = 8;
		// volume #8
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 6, 0, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 7, 2, 3));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 6, 1, 3));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 13, 0, 1));
		
		volumeId = 9;
		// volume #9
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 13, 3, 0));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 7, 2, 1));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 8, 1, 2));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 7, 1, 6));
		
		volumeId = 10;
		// volume #10
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 10, 0, 8));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 7, 1, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 9, 1, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 9, 0, 6));
		
		volumeId = 11;
		// volume #11
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 8, 0, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 10, 1, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 7, 1, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 9, 4, 4));
		
		volumeId = 12;
		// volume #12
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 8, 3, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 4, 1, 9));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 6, 0, 7));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 12, 1, 11));
		
		volumeId = 13;
		// volume #13
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 15, 0, 12));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 8, 2, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 6, 2, 8));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 7, 2, 4));
		
		volumeId = 14;
		// volume #14
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 7, 0, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 6, 1, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 9, 1, 7));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 21, 1, 8));
		
		volumeId = 15;
		// volume #15
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 10, 1, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 6, 2, 7));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 6, 2, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 8, 1, 5));
		
		volumeId = 16;
		// volume #16
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 9, 2, 5));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 11, 0, 6));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 9, 4, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 13, 1, 7));
		
		volumeId = 17;
		// volume #17
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 18, 0, 7));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 9, 0, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 3, 9, 2, 7));
		issuePageTestList.add(new IssuePageTest(volumeId, 4, 10, 2, 3));
		
		volumeId = 18;
		// volume #18
		issuePageTestList.add(new IssuePageTest(volumeId, 1, 12, 4, 4));
		issuePageTestList.add(new IssuePageTest(volumeId, 2, 18, 4, 4));
	}
	
}
