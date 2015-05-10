package mikugo.dev.search.index.mapper;

import java.util.List;

import mikugo.dev.search.model.IndexResultResourcesModel;
import mikugo.dev.search.model.ResultModel;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * This class represents a result model for Users
 * 
 * @author mikugo
 *
 */
public class UserResultMapper extends IndexResultMapperImpl {

    private static Log log = LogFactoryUtil.getLog(UserResultMapper.class);
    private User user;

    public UserResultMapper(IndexResultResourcesModel indexResultResourcesModel) throws Exception {
	super(indexResultResourcesModel);
    }

    @Override
    public ResultModel getResultModel() {
	ResultModel resultModel = super.getResultModel();

	try {
	    this.user = UserLocalServiceUtil.getUser(processor.getAssetEntry().getClassPK());
	    resultModel.setDisplayUrl(user.getDisplayURL(processor.getThemeDisplay()));
	    writeMetadata(resultModel);
	} catch (PortalException e) {
	    resultModel.setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	} catch (SystemException e) {
	    resultModel.setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	}

	return resultModel;
    }

    /**
     * Setting metadata for a {@link UserResultMapper}
     * 
     * @param result
     */
    public void writeMetadata(ResultModel resultModel) {
	try {
	    User user = UserLocalServiceUtil.getUser(processor.getAssetEntry().getClassPK());
	    StringBuilder sb = new StringBuilder();
	    sb.append(user.getEmailAddress());
	    List<Phone> phones = user.getPhones();

	    if (!phones.isEmpty()) {
		sb.append(LanguageUtil.get(processor.getThemeDisplay().getLocale(), "phone"));
		sb.append(" ");

		for (Phone phone : phones) {
		    sb.append(phone);
		}
	    }
	    resultModel.setMetadata(sb.toString());
	} catch (PortalException e) {
	    resultModel.setMetadata("");
	    log.error(e);
	} catch (SystemException e) {
	    log.error(e);
	}
    }

}
