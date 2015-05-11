package com.hamdikavak.data.retrieval.jasss;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Hamdi
 *
 */
public class JASSSDatasetTool {

	private static final Logger logger = LogManager
			.getLogger(JASSSDatasetTool.class);

	public static void main(String[] args) {

		String path = args[0];
		logger.info("Application is starting...");
		JASSSDataExtracter jasssDataExtracter = new JASSSDataExtracter();
		ArrayList<String> issueURLs = jasssDataExtracter.extractIssueURLs();
		ArrayList<IssuePage> issuePages = new ArrayList<IssuePage>();
		IssuePage tempIssue;

		issueURLs = jasssDataExtracter.extractIssueURLs();

		for (String issueURL : issueURLs) {
			logger.info("issue url: " + issueURL);
			tempIssue = jasssDataExtracter.extractIssuePageContent(issueURL);
			issuePages.add(tempIssue);
		}

		int counter;
		String filePath;

		for (IssuePage issue : issuePages) {
			logger.info("Volume:" + issue.getVolumeId() + " - Issue:"
					+ issue.getIssueId());
			logger.info("REFEREED");
			logger.info("---------------------------------------");
			counter = 0;

			for (String articleURL : issue.getRefereedArticleURLs()) {
				logger.info("Peer reviewed article URL: " + articleURL);
				counter++;
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.PeerReviewed);
				filePath = path + System.getProperty("file.separator")
						+ issue.getVolumeId() + "-" + issue.getIssueId() + "-"
						+ myArticle.getType().toString().toLowerCase() + "-"
						+ counter + ".xml";
				JASSSDataExporter.exportArticle(myArticle, filePath);
			}
			logger.info("FORUM");
			logger.info("---------------------------------------");
			for (String articleURL : issue.getForumArticleURLs()) {
				logger.info("Forum article URL: " + articleURL);
				counter++;
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.Forum);
				filePath = path + System.getProperty("file.separator")
						+ issue.getVolumeId() + "-" + issue.getIssueId() + "-"
						+ myArticle.getType().toString().toLowerCase() + "-"
						+ counter + ".xml";
				JASSSDataExporter.exportArticle(myArticle, filePath);
			}

			logger.info("BOOK REVIEW");
			logger.info("---------------------------------------");
			for (String articleURL : issue.getReviewArticleURLs()) {
				logger.info("book review URL: " + articleURL);
				counter++;
				JASSSArticle myArticle = jasssDataExtracter.extractArticle(
						articleURL, issue, ArticleType.Review);
				filePath = path + System.getProperty("file.separator")
						+ issue.getVolumeId() + "-" + issue.getIssueId() + "-"
						+ myArticle.getType().toString().toLowerCase() + "-"
						+ counter + ".xml";
				JASSSDataExporter.exportArticle(myArticle, filePath);
			}
		}
	}
}
