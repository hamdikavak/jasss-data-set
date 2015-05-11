package com.hamdikavak.data.retrieval.jasss;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JASSSDataExtracter {
	
	private static final Logger logger = LogManager.getLogger(JASSSDataExtracter.class);
	private static String DEFAULT_URL = "http://jasss.soc.surrey.ac.uk/index_by_issue.html";
	private String jasssIndexURL;
	
	/**
	 * Constructor setting the default URL
	 */
	public JASSSDataExtracter(){
		this(DEFAULT_URL);
	}
	
	/**
	 * Constructor setting a custom URL. 
	 * Use this method when JASSS updates the index page. 
	 */
	public JASSSDataExtracter(String jasssIndexURL){
		this.jasssIndexURL = jasssIndexURL;
	}
	
	/**
	 * Extracts issue URLs from index page
	 * @return List of issue URLs
	 */
	public ArrayList<String> extractIssueURLs(){
		logger.info("Extracting all issue URLs");
		ArrayList<String> issueURLs = new ArrayList<String>();
		
		try {
			Document doc = Jsoup.connect(jasssIndexURL).get();
			for(Element el: doc.getElementsByAttributeValueEnding("href", "contents.html") ){	
				issueURLs.add(el.attr("href"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return issueURLs;
	}
	
	/**
	 * Extracts article lists of the issue page
	 * @param url
	 * @return
	 */
	public IssuePage extractIssuePageContent(String url){
		IssuePage issuePage = new IssuePage();
		ArrayList<String> refereedURLs = new ArrayList<String>();
		ArrayList<String> forumURLs = new ArrayList<String>();
		ArrayList<String> reviewURLs = new ArrayList<String>();
		
		try {
			Document doc = Jsoup.connect(url).get();
			
			int mode = 0; // 0: nothing, 1: refereed article, 2: forum, 3: book review 
			for(Element el: doc.getAllElements()){
				
				if(el.tagName().toLowerCase().equals("h1") && el.text().toLowerCase().contains("volume")){ 
					// h1 tag shows volume and issue number 
					String volumeIssueString = el.text().trim();
					
					String[] volumeIssue = volumeIssueString.split(", ");
					String vol = volumeIssue[0].toLowerCase().replace("volume", "").trim();
					String iss = volumeIssue[1].toLowerCase().replace("issue", "").trim();
					
					issuePage.setVolumeId(Integer.parseInt(vol));
					
					if(iss.startsWith("s")){ 
						// this is an exceptional case in volume 2, where issues 3 and 4 are merged in a single content page  
						// the above issue extraction method finds "s 3 and 4" as the issue Id. We use issue id as 3 for both issues
						issuePage.setIssueId(3);
					}
					else{
						issuePage.setIssueId(Integer.parseInt(iss));
					}
					
				}
				
				else if(el.tagName().toLowerCase().equals("h2") || el.tagName().toLowerCase().equals("h3")){
					if(el.text().toLowerCase().contains("articles") == true || el.text().toLowerCase().contains("essay") == true){
						mode = 1;
					}
					else if(el.text().toLowerCase().contains("forum") == true){
						mode = 2;
					}
					else if(el.text().toLowerCase().contains("review") == true){
						mode = 3;
					}
				}
				else if (el.tagName().toLowerCase().equals("b") && el.text().toLowerCase().startsWith ("important note about") ){
					mode = 1;
				}
				else if( el.tagName() == "a" && el.attr("href").trim().startsWith("../") == false && el.attr("href").trim().startsWith("/") == false){
					if(mode == 1){
						refereedURLs.add(el.attr("abs:href"));
					}
					else if(mode == 2){
						forumURLs.add(el.attr("abs:href"));
					}
					else if(mode == 3){
						reviewURLs.add(el.attr("abs:href"));
					}
				}
			}
			issuePage.setRefereedArticleURLs(refereedURLs);
			issuePage.setReviewArticleURLs(reviewURLs);
			issuePage.setForumArticleURLs(forumURLs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return issuePage;
	}
	
	/**
	 * Extracts the article content based on URL and article type.
	 * @param url URL of the article.
	 * @param issuePage reference to the issue page of the article.
	 * @param type article type.
	 * @return
	 */
	public JASSSArticle extractArticle(String url, IssuePage issuePage, ArticleType type){
		JASSSArticle jasssArticle = new JASSSArticle();
		
		jasssArticle.setIssuePage(issuePage);
		jasssArticle.setType(type);
		
		try {
			Document doc = Jsoup.connect(url).get();
			jasssArticle.setURL(url);
			jasssArticle.setTitle(doc.select("[name=DC.Title]").attr("content"));
			String desc = doc.select("[name=DC.Description]").attr("content");
			String abstractText = getAbstractText(desc, doc.getAllElements());
			jasssArticle.setAbstractText(abstractText);
			
			if(type != ArticleType.Review){ // reviews do not have keywords 
				String keywordsString = doc.select("[name=DC.Subject]").attr("content").trim();
				String[] keywords = separateKeywords(keywordsString);
				jasssArticle.setKeywords(keywords);
			}
			
			String authorsString = doc.select("[name=DC.Creator]").attr("content").trim();
			String[] authors = separateAuthors(authorsString);
			jasssArticle.setAuthors(authors);
			
			String dateString = doc.select("[name=DC.Date]").attr("content").trim();
			dateString = dateString.replace(" ", "");
			int year = extractYearFromDateMetaTag(dateString);
			if(type != ArticleType.Review && year == 0){
				// it means the meta tag came empty or there is another issue.
				// get the date from the <body>
				year = extractYearFromBody(doc);
			}
			jasssArticle.setPublicationYear(year);
			
			String articleText = extractArticleText(doc,  type);
			jasssArticle.setContentText(articleText);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jasssArticle;
	}
	
	private int extractYearFromBody(Document doc) {
		
		Elements elm = doc.select("p:contains(published):contains(received)"); // this should select the p element containing the publish date
		try{
			String datePublished = elm.get(0).text().trim().split("Published:")[1].trim();
			return extractYearFromDateMetaTag(datePublished);
		}
		catch(Exception ex){
			logger.info("Exception:" + ex.getLocalizedMessage());
		}
		
		return 0;
	}

	private String getAbstractText(String desc, Elements allElements) {
		
		if(desc.equals("301")){
			// This is a special case in the first issue. Here description does not contain the abstract.

			boolean abstractFound = false;
			int i = 0;
			
			for(i=0; i< allElements.size(); i++){
				if(allElements.get(i).text().toLowerCase().equals("abstract")){
					abstractFound = true;
					break;
				}
			}
			
			if(abstractFound == true){
				return allElements.get(i+2).text();
			}
			return null;
		}
		else if(desc.startsWith("[No abstract")){
			return "";
		}
		else{
			return desc.trim();
		}
	}

	private String extractArticleText(Document doc, ArticleType type) {
		StringBuilder docText = new StringBuilder();
		String prevElementText ;
		Elements elems =  doc.select("dl[compact]");
		
		
		for(Element el: elems){
			Element prevElem = el.previousElementSibling();

			
			if(prevElem != null ){
				prevElementText = prevElem.text().trim().toLowerCase();
				
				if(prevElementText.contains("abstract") == false && prevElementText.contains("reference") == false &&
						prevElementText.contains("acknowled") == false && prevElementText.contains("abstract") == false && 
						el.text().toLowerCase().startsWith("keywords") == false){
					docText.append(el.text());
				}
			}
		}
	
		if(docText.toString().length() == 0 && type == ArticleType.Review){
			elems =  doc.getAllElements();
			boolean getText = false;
			for(Element el: elems){
				if(el.tagName().equals("p") && el.text().toLowerCase().contains("reviewed by")){
					getText = true;
					continue;
				}
				else if(getText == true && el.tagName().toLowerCase().equals("h3") && el.text().toLowerCase().contains("references")){
					break;
				}
				
				if(getText == true){
					docText.append(el.text());
				}
			}
		}
		
		return docText.toString();
	}

	private int extractYearFromDateMetaTag(String dateString) {
		// There are two date formats in articles
		// 1) d-mmm-yy(earlier format)
		// 2) d-mmm-yyyy(another earlier format)
		// 3) yyyy-MM-dd(current format)
		DateTimeFormatter oldFormatter = new DateTimeFormatterBuilder()
		        .appendDayOfMonth(2)
		        .appendLiteral('-')
		        .appendMonthOfYearShortText()
		        .appendLiteral('-')
		        .appendTwoDigitYear(2000)
		        .toFormatter();
		DateTimeFormatter anotherOldFormatter = new DateTimeFormatterBuilder()
		        .appendDayOfMonth(2)
		        .appendLiteral('-')
		        .appendMonthOfYearShortText()
		        .appendLiteral('-')
		        .appendYear(4, 4)
		        .toFormatter();
		DateTimeFormatter currentFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		LocalDate date;
		
		// the most current date representation.
		try{
			date = LocalDate.parse(dateString, currentFormatter);
			return date.getYear();
		}
		catch(Exception ex){
			//logger.info("Current date parsing failed");
		}
		
		// the older date representation.
		try{
			date = LocalDate.parse(dateString, oldFormatter);
			return date.getYear();
		}
		catch(Exception ex2){
			//logger.info("Older date parsing failed");
			
		}
		
		try{
			date = LocalDate.parse(dateString, anotherOldFormatter);
			return date.getYear();
		}
		catch(Exception ex2){
			//logger.info("Older date parsing failed");
			return 0;
		}
		
	}

	private String[] separateKeywords(String keywordsString) {
		String[] allkeywords = null;
		// we may have different keyword splitters such as ; or ,
		if(keywordsString.contains(";")){
			allkeywords = keywordsString.split("; ");
		}
		else if(keywordsString.contains(",")){
			allkeywords = keywordsString.split(", ");
		}
		else{
			// if we are here, it means the article has only 1 keyword
			allkeywords = new String[1];
			allkeywords[0] = keywordsString;
		}
		
		for(int i=0; i<allkeywords.length; i++){
			allkeywords[i] = allkeywords[i].trim();
		}
		
		return allkeywords;
	}

	private String[] separateAuthors(String authorsString) {
		String[] authorsSplitted = authorsString.split(" and ");
		
		// check if there is only one author
		
		if(authorsSplitted.length == 1){
			authorsSplitted[0] = authorsSplitted[0].trim();
			return authorsSplitted;
		}
		
		String[] firstAuthors = authorsSplitted[0].split(", ");
		String[] allAuthors = new String[firstAuthors.length+1];
		int i;
		for(i=0; i<firstAuthors.length; i++){
			allAuthors[i] = firstAuthors[i].trim();
		}
		allAuthors[i] = authorsSplitted[1].trim();
		
		return allAuthors;
	}

}
