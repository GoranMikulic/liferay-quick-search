package mikugo.dev.search.helper;

public enum AssetTypes {
    
    USER("user", "com.liferay.portal.model.User"),
    FILEENTRY("file", "com.liferay.portlet.documentlibrary.model.DLFileEntry"),
    JOURNAL_ARTICLE("article", "com.liferay.portlet.journal.model.JournalArticle");
    

    private String readableName;
    private String className;

    private AssetTypes(String readableName, String className) {
	this.readableName = readableName;
	this.className = className;
    }

    public String getReadableName() {
	return readableName;
    }
   
    public String getClassName() {
	return className;
    }
    
    public static String[] getAllClassNames() {
	return new String[]{USER.className, FILEENTRY.className, JOURNAL_ARTICLE.className};
    }
}
