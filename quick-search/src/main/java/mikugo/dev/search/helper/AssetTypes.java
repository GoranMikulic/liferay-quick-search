package mikugo.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumartaion class for the available search asset types
 * 
 * @author mikugo
 *
 */
public enum AssetTypes {

    USER("user", "com.liferay.portal.model.User"), FILEENTRY("file",
	    "com.liferay.portlet.documentlibrary.model.DLFileEntry"), JOURNAL_ARTICLE("article",
	    "com.liferay.portlet.journal.model.JournalArticle"), BOOKMARKS_ENTRY("bookmark",
	    "com.liferay.portlet.bookmarks.model.BookmarksEntry"), BLOGS_ENTRY("blog",
	    "com.liferay.portlet.blogs.model.BlogsEntry"), MBM_MESSAGE("mbmessage",
	    "com.liferay.portlet.messageboards.model.MBMessage"), WIKI_PAGE("wiki-page",
	    "com.liferay.portlet.wiki.model.WikiPage"), SITE("site", "com.liferay.portal.model.Group"), LAYOUT("page",
	    "com.liferay.portal.model.Layout");

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

    public static String getReadableName(String className) {
	for (AssetTypes type : AssetTypes.values()) {
	    if (className.equals(type.className)) {
		return type.readableName;
	    }
	}

	return null;
    }

    public static String[] getReadableNames(String[] classNames) {
	String[] readableNames = new String[classNames.length];

	for (int i = 0; i < classNames.length; i++) {
	    readableNames[i] = getReadableName(classNames[i]);
	}

	return readableNames;
    }

    public static String[] getAllClassNames() {

	List<String> types = new ArrayList<String>();
	for (AssetTypes type : values()) {
	    types.add(type.className);
	}
	return types.toArray(new String[types.size()]);
    }

    public static String[] getIndexSearchClassNames() {
	return new String[] { USER.className, FILEENTRY.className, JOURNAL_ARTICLE.className,
		BOOKMARKS_ENTRY.className, BLOGS_ENTRY.className, MBM_MESSAGE.className, WIKI_PAGE.className };
    }

    public static String[] getIndexSearchReadableNames() {
	return new String[] { USER.readableName, FILEENTRY.readableName, JOURNAL_ARTICLE.readableName,
		BOOKMARKS_ENTRY.readableName, BLOGS_ENTRY.readableName, MBM_MESSAGE.readableName,
		WIKI_PAGE.readableName };
    }

    public static String[] getDynamicQueryClassNames() {
	return new String[] { SITE.className, LAYOUT.className };
    }

    public static String[] getDynamicQueryReadableNames() {
	return new String[] { SITE.readableName, LAYOUT.readableName };
    }
}
