package xsm.axis.v201306.baserateservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201306.StatementBuilder;
import com.google.api.ads.dfp.axis.v201306.BaseRate;
import com.google.api.ads.dfp.axis.v201306.BaseRatePage;
import com.google.api.ads.dfp.axis.v201306.BaseRateServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example gets all base rates. To create base rates, run
 * CreateBaseRates.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: BaseRateService.getBaseRatesByStatement
 *
 * @author Oscar Xie
 */
public class GetAllBaseRates {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the BaseRateService.
	  BaseRateServiceInterface baseRateService =
        dfpServices.get(session, BaseRateServiceInterface.class);

    // Create a statement to select all base rates.
    StatementBuilder statementBuilder = new StatementBuilder()
        .orderBy("id ASC")
        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

    // Default for total result set size.
    int totalResultSetSize = 0;

    do {
      // Get rate cards by statement.
    	BaseRatePage page =
    			baseRateService.getBaseRatesByStatement(statementBuilder.toStatement());

      if (page.getResults() != null) {
        totalResultSetSize = page.getTotalResultSetSize();
        int i = page.getStartIndex();
        for (BaseRate baserate : page.getResults()) {
          System.out.printf(
              "%d) Rate Card with ID \"%d\" and Base Rate with ID \"%d\" Type \"%s\" Status \"%s\" was found.\n",
              i++, baserate.getRateCardId(), baserate.getId(), baserate.getBaseRateType(), baserate.getStatus());
        }
      }

      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
    } while (statementBuilder.getOffset() < totalResultSetSize);

    System.out.printf("Number of results found: %d\n", totalResultSetSize);
  }

  public static void main(String[] args) throws Exception {
    // Generate a refreshable OAuth2 credential similar to a ClientLogin token
    // and can be used in place of a service account.
    Credential oAuth2Credential = new OfflineCredentials.Builder()
        .forApi(Api.DFP)
        .fromFile()
        .build()
        .generateCredential();

    // Construct a DfpSession.
    DfpSession session = new DfpSession.Builder()
        .fromFile()
        .withOAuth2Credential(oAuth2Credential)
        .build();

    DfpServices dfpServices = new DfpServices();

    runExample(dfpServices, session);
  }
}
