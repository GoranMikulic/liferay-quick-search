package mikugo.dev.search.helper;

public enum AssetTypes {

    USER("user", "com.liferay.portal.model.User"), FILEENTRY("file",
	    "com.liferay.portlet.documentlibrary.model.DLFileEntry"), JOURNAL_ARTICLE("article",
	    "com.liferay.portlet.journal.model.JournalArticle"), BOOKMARKS_ENTRY("bookmark",
	    "com.liferay.portlet.bookmarks.model.BookmarksEntry"), BLOGS_ENTRY("blog",
	    "com.liferay.portlet.blogs.model.BlogsEntry"), MBM_MESSAGE("message-board-messages",
	    "com.liferay.portlet.messageboards.model.MBMessage"), WIKI_PAGE("wiki-page",
	    "com.liferay.portlet.wiki.model.WikiPage"), SITE("site", "com.liferay.portal.model.Group"), LAYOUT(
	    "layout", "com.liferay.portal.model.Layout");

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

    public static String getClassName(String readableName) {

	for (AssetTypes type : AssetTypes.values()) {
	    if (readableName.equals(type.readableName)) {
		return type.className;
	    }
	}

	return null;
    }

    public static String[] getAllClassNames() {
	return new String[] { USER.className, FILEENTRY.className, JOURNAL_ARTICLE.className,
		BOOKMARKS_ENTRY.className, BLOGS_ENTRY.className, MBM_MESSAGE.className, WIKI_PAGE.className };
    }
}
