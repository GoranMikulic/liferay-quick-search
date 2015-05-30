package mikugo.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration class for the available search asset types
 * 
 * @author mikugo
 *
 */
public enum AssetTypes {

    /** index search types **/
    USER("user", "com.liferay.portal.model.User", true), // <br/>
    FILEENTRY("file", "com.liferay.portlet.documentlibrary.model.DLFileEntry", true), // <br/>
    JOURNAL_ARTICLE("article", "com.liferay.portlet.journal.model.JournalArticle", true), // <br/>
    BOOKMARKS_ENTRY("bookmark", "com.liferay.portlet.bookmarks.model.BookmarksEntry", true), // <br/>
    BLOGS_ENTRY("blog", "com.liferay.portlet.blogs.model.BlogsEntry", true), // <br/>
    MBM_MESSAGE("mbmessage", "com.liferay.portlet.messageboards.model.MBMessage", true), // <br/>
    WIKI_PAGE("wiki-page", "com.liferay.portlet.wiki.model.WikiPage", true), // <br/>

    /** dynamic query types **/
    SITE("site", "com.liferay.portal.model.Group", false), // <br/>
    LAYOUT("page", "com.liferay.portal.model.Layout", false);

    private String readableName;
    private String className;
    boolean isIndexSearch;

    private AssetTypes(String readableName, String className, boolean isIndexSearch) {
	this.readableName = readableName;
	this.className = className;
	this.isIndexSearch = isIndexSearch;
    }

    public String getReadableName() {
	return readableName;
    }

    public String getClassName() {
	return className;
    }

    /**
     * Returns class name of the readable name
     * 
     * @param readableName
     *            - The readable name of the searched class name
     * @return class name of the class name
     */
    public static String getClassName(String readableName) {

	for (AssetTypes type : AssetTypes.values()) {
	    if (readableName.equals(type.readableName)) {
		return type.className;
	    }
	}

	return null;
    }

    /**
     * Returns readable name of the className
     * 
     * @param className
     *            - The className of the searched readableName
     * @return readable name of the className
     */
    public static String getReadableName(String className) {
	for (AssetTypes type : AssetTypes.values()) {
	    if (className.equals(type.className)) {
		return type.readableName;
	    }
	}

	return null;
    }

    /**
     * Returns {@link String[]} of type names of the given classNames
     * 
     * @param classNames
     *            - ClassNames the readable names are searched for
     * @return {@link String[]} of type names of the given classNames
     */
    public static String[] getReadableNames(String[] classNames) {
	String[] readableNames = new String[classNames.length];

	for (int i = 0; i < classNames.length; i++) {
	    readableNames[i] = getReadableName(classNames[i]);
	}

	return readableNames;
    }

    /**
     * Returns all class names
     * 
     * @return all class names
     */
    public static String[] getAllClassNames() {

	List<String> types = new ArrayList<String>();
	for (AssetTypes type : values()) {
	    types.add(type.className);
	}
	return types.toArray(new String[types.size()]);
    }

    /**
     * Returns class names of index search types
     * 
     * @return class names of index search types
     */
    public static String[] getIndexSearchClassNames() {

	List<String> indexClassNames = new ArrayList<String>();

	for (AssetTypes type : values()) {
	    if (type.isIndexSearch) {
		indexClassNames.add(type.className);
	    }
	}

	return indexClassNames.toArray(new String[indexClassNames.size()]);
    }

    /**
     * Returns names of index search types
     * 
     * @return names of index search types
     */
    public static String[] getIndexSearchReadableNames() {

	List<String> indexReadableNames = new ArrayList<String>();

	for (AssetTypes type : values()) {
	    if (type.isIndexSearch) {
		indexReadableNames.add(type.readableName);
	    }
	}

	return indexReadableNames.toArray(new String[indexReadableNames.size()]);
    }

    /**
     * Returns class names of dynamic query types
     * 
     * @return class names of dynamic query types
     */
    public static String[] getDynamicQueryClassNames() {

	List<String> dynamicQueryClassNames = new ArrayList<String>();

	for (AssetTypes type : values()) {
	    if (!type.isIndexSearch) {
		dynamicQueryClassNames.add(type.className);
	    }
	}

	return dynamicQueryClassNames.toArray(new String[dynamicQueryClassNames.size()]);
    }

    /**
     * Returns all names of query types
     * 
     * @return Names of query types
     */
    public static String[] getDynamicQueryReadableNames() {
	List<String> dynamicQueryReadableNames = new ArrayList<String>();

	for (AssetTypes type : values()) {
	    if (!type.isIndexSearch) {
		dynamicQueryReadableNames.add(type.readableName);
	    }
	}

	return dynamicQueryReadableNames.toArray(new String[dynamicQueryReadableNames.size()]);
    }
}
