package mikugo.dev.search.data;

import java.util.ArrayList;
import java.util.List;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class LayoutSearchImpl extends AbstractDynamicQuerySearch implements Search {

    private static final String CRITERION_FRIENDLYURL = "friendlyURL";

    public LayoutSearchImpl(String pattern, int maxSearchResults, ThemeDisplay themeDisplay) {
	super(pattern, maxSearchResults, themeDisplay);
    }

    @Override
    public List<ResultModel> getResult() throws SearchException, Exception {
	// TODO: set fuzzy search without string manipuliation
	pattern = "%" + pattern + "%";
	DynamicQuery query = DynamicQueryFactoryUtil.forClass(Class.forName(AssetTypes.LAYOUT.getClassName())).add(
		PropertyFactoryUtil.forName(CRITERION_FRIENDLYURL).like(this.pattern));

	@SuppressWarnings("unchecked")
	List<Layout> result = LayoutLocalServiceUtil.dynamicQuery(query, 0, this.maxSearchResults);

	List<ResultModel> resultModel = new ArrayList<ResultModel>();

	for (Layout layout : result) {

	    if (LayoutPermissionUtil.contains(themeDisplay.getPermissionChecker(), layout.getPlid(), "VIEW")
		    && layout.getGroup().isSite()) {

		resultModel.add(new ResultModel(layout.getName(), layout.getDescription(), "/web"
			+ layout.getGroup().getFriendlyURL() + layout.getFriendlyURL(), LanguageUtil.get(
			themeDisplay.getLocale(), AssetTypes.LAYOUT.getReadableName()), layout.getGroup()
			.getDescriptiveName()));
	    }
	}
	return resultModel;
    }

}
