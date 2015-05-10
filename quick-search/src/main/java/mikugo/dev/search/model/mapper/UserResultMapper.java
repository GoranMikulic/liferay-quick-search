package mikugo.dev.search.model.mapper;

import java.util.List;

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
public class UserResultMapper extends ResultModel {

    private static Log log = LogFactoryUtil.getLog(UserResultMapper.class);
    private User user;

    public UserResultMapper(LiferayIndexSearchResultProcessor result) {
	super(result);
	try {
	    this.user = UserLocalServiceUtil.getUser(result.getAssetEntry().getClassPK());
	    setDisplayUrl(user.getDisplayURL(result.getThemeDisplay()));
	    writeMetadata(result);
	} catch (PortalException e) {
	    setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	} catch (SystemException e) {
	    setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	}
    }

    /**
     * Setting metadata for a {@link UserResultMapper}
     * 
     * @param result
     */
    public void writeMetadata(LiferayIndexSearchResultProcessor result) {
	try {
	    User user = UserLocalServiceUtil.getUser(result.getAssetEntry().getClassPK());
	    StringBuilder sb = new StringBuilder();
	    sb.append(user.getEmailAddress());
	    List<Phone> phones = user.getPhones();

	    if (!phones.isEmpty()) {
		sb.append(LanguageUtil.get(result.getThemeDisplay().getLocale(), "phone"));
		sb.append(" ");

		for (Phone phone : phones) {
		    sb.append(phone);
		}
	    }
	    setMetadata(sb.toString());
	} catch (PortalException e) {
	    setMetadata("");
	    log.error(e);
	} catch (SystemException e) {
	    log.error(e);
	}
    }
}
