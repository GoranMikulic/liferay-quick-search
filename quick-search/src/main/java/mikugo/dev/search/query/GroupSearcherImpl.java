package mikugo.dev.search.query;

import java.util.ArrayList;
import java.util.List;

import mikugo.dev.search.data.Search;
import mikugo.dev.search.helper.AssetTypes;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * Implementation for a {@link Group} search
 * 
 * @author Goran Mikulic
 *
 */
public class GroupSearcherImpl extends AbstractDynamicQuerySearch implements Search {

    private static final String CRITERION_NAME = "name";

    public GroupSearcherImpl(String pattern, int maxSearchResults, ThemeDisplay themeDisplay) {
	super(pattern, maxSearchResults, themeDisplay);
    }

    @Override
    public List<ResultModel> getResult() throws SystemException, PortalException, ClassNotFoundException {
	// TODO: set fuzzy search without string manipuliation
	pattern = "%" + pattern + "%";
	DynamicQuery query = DynamicQueryFactoryUtil.forClass(Class.forName(AssetTypes.SITE.getClassName())).add(
		PropertyFactoryUtil.forName(CRITERION_NAME).like(pattern));

	@SuppressWarnings("unchecked")
	List<Group> result = GroupLocalServiceUtil.dynamicQuery(query, 0, this.maxSearchResults);
	List<ResultModel> resultModel = new ArrayList<ResultModel>();

	for (Group group : result) {
	    if (GroupPermissionUtil.contains(themeDisplay.getPermissionChecker(), group, "VIEW")
		    && !group.getFriendlyURL().equals("/global") && group.isSite()) {

		resultModel.add(new GroupMapper(themeDisplay).getResultModel(group));

	    }
	}

	return resultModel;
    }

}
