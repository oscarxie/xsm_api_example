package xsm.axis.v201306.networkservice;

import org.testng.annotations.*;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.conf.ConfigurationLoadException;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

public class networkserviceTest {

    private DfpSession session;
    private DfpServices dfpServices;

    @BeforeMethod
    public void setUp() throws ConfigurationLoadException, ValidationException, OAuthException {
        // Generate a refreshable OAuth2 credential similar to a ClientLogin token
        // and can be used in place of a service account.
        Credential oAuth2Credential = new OfflineCredentials.Builder()
            .forApi(Api.DFP)
            .fromFile()
            .build()
            .generateCredential();

        // Construct a DfpSession.
        session = new DfpSession.Builder()
            .fromFile()
            .withOAuth2Credential(oAuth2Credential)
            .build();

        dfpServices = new DfpServices();
    }

    @Test(groups = {"networkservice"})
    public void getAllNetworks() throws Exception {
        // Remove any network code set from the ads.properties file.
        session.setNetworkCode(null);
        GetAllNetworks.runExample(dfpServices, session);
    }

    @Test(groups = {"networkservice"})
    public void getCurrentNetwork() throws Exception {
        GetCurrentNetwork.runExample(dfpServices, session);
    }

    @AfterMethod
    public void tearDown() {
        session = null;
        dfpServices = null;
    }

}
