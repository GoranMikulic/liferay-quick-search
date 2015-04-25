package mikugo.dev.search.model;

import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Phone;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * This class represents a result model for {@link User}
 * 
 * @author mikugo
 *
 */
public class UserResultModel extends ResultModel {

    private static Log log = LogFactoryUtil.getLog(UserResultModel.class);
    private User user;

    public UserResultModel(IndexSearchResult result) {
	super(result);
	try {
	    this.user = UserLocalServiceUtil.getUser(result.getAssetEntry().getClassPK());
	    setDisplayUrl(user.getDisplayURL(result.getThemeDisplay()));
	} catch (PortalException e) {
	    setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	} catch (SystemException e) {
	    setDisplayUrl("mailto:" + user.getDisplayEmailAddress());
	    log.error(e);
	}
    }

    @Override
    public void writeMetadata(IndexSearchResult result) {
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
