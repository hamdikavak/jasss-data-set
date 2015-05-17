# JASSS Data Set
This tool downloads and organizes articles published in the open journal called "Journal of Artificial Societies and Social Simulation" ([JASSS](jasss.soc.surrey.ac.uk))

Usage:

```
java -cp jasss-dataset-tool.jar com.hamdikavak.data.retrieval.jasss.JASSSDatasetTool /path/to/the/output/folder
```

The tool will generate 962 files (one per article) in XML format. The articles are in one of these categories: peer reviewed, forum, and book review.Based on my quick search, I found that eight articles in the dataset did not have a content. That is due to inconsistent formatting in JASSS. I manually added the content for those articles. I will not publicly provide the dataset here unless I get approval from JASSS editors. You can always email me if you want to obtain the dataset.

## Warning
This software is developed for educational purposes and the developer is not affiliated with JASSS. The developer got writtten approval from the editorial of JASSS regarding crawling JASSS articles. It's user's own responsibility to get permission from the publisher.
