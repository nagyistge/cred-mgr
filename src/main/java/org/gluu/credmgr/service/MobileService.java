package org.gluu.credmgr.service;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.gluu.credmgr.domain.OPConfig;
import org.gluu.credmgr.service.error.OPException;
import org.gluu.oxtrust.model.scim2.Constants;
import org.gluu.oxtrust.model.scim2.ExtensionFieldType;
import org.gluu.oxtrust.model.scim2.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugeniuparvan on 8/26/16.
 */
@Service
public class MobileService {

    public void sendPasswordResetSMS(User user, String baseUrl, OPConfig opConfig) throws OPException {
        try {
            String resetKey = user.getExtensions().get(Constants.USER_EXT_SCHEMA_ID).getField("resetKey", ExtensionFieldType.STRING);
            TwilioRestClient client = new TwilioRestClient(opConfig.getTwilioSID(), opConfig.getTwilioToken());

            // Build the parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("To", user.getPhoneNumbers().get(0).getValue()));
            params.add(new BasicNameValuePair("From", opConfig.getTwilioFromNumber()));
            params.add(new BasicNameValuePair("Body", "Proceed to reset your password: " + baseUrl + "/#/reset/finish?key=" + resetKey));

            MessageFactory messageFactory = client.getAccount().getMessageFactory();
            messageFactory.create(params);
        } catch (Exception e) {
            throw new OPException(OPException.ERROR_SEND_SMS);
        }
    }
}
