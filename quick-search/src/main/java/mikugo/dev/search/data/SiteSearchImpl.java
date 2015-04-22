package mikugo.dev.search.data;

import java.util.ArrayList;
import java.util.List;

import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;

public class SiteSearchImpl extends AbstractDynamicQuerySearch implements Search {

    private static final String CRITERION_NAME = "name";

    public SiteSearchImpl(String pattern, int maxSearchResults, ThemeDisplay themeDisplay) {
	super(pattern, maxSearchResults, themeDisplay);
    }

    @Override
    public List<ResultModel> getResult() throws SystemException, PortalException, ClassNotFoundException {
	
	DynamicQuery query = DynamicQueryFactoryUtil.forClass(Class.forName(AssetTypes.SITE.getClassName())).add(
		PropertyFactoryUtil.forName(CRITERION_NAME).eq(pattern));
	
	@SuppressWarnings("unchecked")
	List<Group> result = GroupLocalServiceUtil.dynamicQuery(query, 0, this.maxSearchResults);
	List<ResultModel> resultModel = new ArrayList<ResultModel>();

	for (Group group : result) {
	    resultModel.add(new ResultModel(group.getDescriptiveName(), group.getDescription(), "/web"
		    + group.getFriendlyURL(), LanguageUtil.get(themeDisplay.getLocale(),
		    AssetTypes.SITE.getReadableName()), ""));
	}

	return resultModel;
    }

}
