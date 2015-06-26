package com.hamdikavak.data.retrieval.jasss;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.thoughtworks.xstream.XStream;

/**
 * This class handles data filtering of the articles that are already downloaded.
 * 
 * @author Hamdi Kavak
 *
 */
public class JASSSDatasetFilteringTool {

	private static final Logger logger = LogManager
			.getLogger(JASSSDatasetFilteringTool.class);
	
	/**
	 * This method filters the dataset by given criteria.
	 * @param requires 4 parameters in order: 
	 * 1: dataset input path, e.g., /path/to/the/folder
	 * 2: dataset output path, e.g., /path/to/the/folder,
	 * 3: article type criteria (* for all), e.g., fp (f:forum p: peer review, b: book review).
	 * 4: properties requested (* for all), e.g., TyivAc (T: title, t: type, y: year, iv: issue and volume, A: authors, k: keywords, a: abstract, c: content)
	 */
	public static void main(String[] args) {
		logger.info("Application is starting...");
		
		if(args == null || args.length != 4){
			logger.error("Please enter all four parameters");
			System.exit(0);
		}
		
		try{
			String inputFolderPath = args[0];
			String outputFolderPath = args[1];
			String articleTypeCriteria = args[2];
			String articlePropertySelection = args[3];
	
			File inputfolder = new File(inputFolderPath);
			File outputFolder = new File(outputFolderPath);
			File[] inputFiles = inputfolder.listFiles();
			JASSSArticle article;
			
			XStream xstream = new XStream();
			xstream.processAnnotations(IssuePage.class);
			xstream.alias("article", JASSSArticle.class);
			
			logger.info("Reading files ...");
			
			int count = 0;
			for(File articleFile: inputFiles){
				logger.info("File: " + articleFile.getName() );
				// convert article xml to java object
				article = (JASSSArticle)xstream.fromXML(articleFile);
				
				if( satisfiesArticleTypeCriteria(article, articleTypeCriteria) == true){
					
					String filename = generateFilename(article, count++);
					String fullFilePath = outputFolder + System.getProperty("file.separator") + filename;
					
					article = selectArticleProperties(article, articlePropertySelection);
					JASSSDataExporter.exportArticle(article, fullFilePath);
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private static String generateFilename(JASSSArticle article, int counter) {
		
		
		return article.getPublicationYear() + "_" + article.getType().toString() + "_" + 
		article.getIssuePage().getVolumeId()+ "_" + article.getIssuePage().getIssueId() + "_" + counter+ ".xml";
	}

	/**
	 * This method
	 * @param article
	 * @param articlePropertySelection
	 * @return
	 */
	private static JASSSArticle selectArticleProperties(JASSSArticle article,
			String articlePropertySelection) {
		
		// deep copy article object 
		XStream xs = new XStream();
		JASSSArticle resultingArticle = (JASSSArticle)xs.fromXML(xs.toXML(article));
		//(T: title, t: type, y: year, iv: issue and volume, A: authors, k: keywords, a: abstract, c: content)
		
		if (articlePropertySelection.contains("T") == false ){
			resultingArticle.setTitle(null);
		}
		if (articlePropertySelection.contains("t") == false ){
			resultingArticle.setType(null);
		}
		if (articlePropertySelection.contains("y") == false ){
			resultingArticle.setPublicationYear(null);
		}
		if (articlePropertySelection.contains("iv") == false){
			resultingArticle.setIssuePage(null);
		}
		if (articlePropertySelection.contains("T") == false ){
			resultingArticle.setTitle(null);
		}
		if (articlePropertySelection.contains("A") == false ){
			resultingArticle.setAuthors(null);
		}
		if (articlePropertySelection.contains("k") == false ){
			resultingArticle.setKeywords(null);
		}
		if (articlePropertySelection.contains("a") == false ){
			resultingArticle.setAbstractText(null);
		}
		if (articlePropertySelection.contains("c") == false ){
			resultingArticle.setContentText(null);
		}
		
		// disabled default
		resultingArticle.setURL(null);
		
		return resultingArticle;
	}

	/**
	 * Determines if the article is one of the requested types.
	 * @param article
	 * @param articleTypeCriteria
	 * @return
	 */
	private static boolean satisfiesArticleTypeCriteria (JASSSArticle article, String articleTypeCriteria) {
		
		return (articleTypeCriteria.toLowerCase().contains("f") && article.getType() == ArticleType.Forum) ||
				(articleTypeCriteria.toLowerCase().contains("p") && article.getType() == ArticleType.PeerReviewed) ||
				(articleTypeCriteria.toLowerCase().contains("b") && article.getType() == ArticleType.Review) || 
				(articleTypeCriteria.contains("*") == true);
	}
}
