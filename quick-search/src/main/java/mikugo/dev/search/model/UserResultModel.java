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

public class UserResultModel extends ResultModel {
    private static Log log = LogFactoryUtil.getLog(UserResultModel.class);

    public UserResultModel(Result result) {
	super(result);

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
