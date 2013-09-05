package xsm.axis.v201306.ratecardcustomizationservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201306.StatementBuilder;
import com.google.api.ads.dfp.axis.v201306.RateCardCustomization;
import com.google.api.ads.dfp.axis.v201306.RateCardCustomizationPage;
import com.google.api.ads.dfp.axis.v201306.RateCardCustomizationServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example gets all rate card customizations. To create rate card customizations, run
 * CreateRateCardCustomizations.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: RateCardCustomizationService.getRateCardCustomizationsByStatement
 *
 * @author Oscar Xie
 */
public class GetAllRateCardCustomizations {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the RateCardCustomizationService.
	  RateCardCustomizationServiceInterface rateCardCustomizationService =
        dfpServices.get(session, RateCardCustomizationServiceInterface.class);

    // Create a statement to select all rate card customizations.
    StatementBuilder statementBuilder = new StatementBuilder()
        .orderBy("id ASC")
        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

    // Default for total result set size.
    int totalResultSetSize = 0;

    do {
      // Get rate cards by statement.
    	RateCardCustomizationPage page =
    			rateCardCustomizationService.getRateCardCustomizationsByStatement(statementBuilder.toStatement());

      if (page.getResults() != null) {
        totalResultSetSize = page.getTotalResultSetSize();
        int i = page.getStartIndex();
        for (RateCardCustomization rateCardCustomizaion : page.getResults()) {
          System.out.printf(
              "%d) Rate Card with ID \"%d\" and Rate Card Customzaion with ID \"%d\" Status \"%s\"" +
              "and AdjustmentSize \"%s\" AdjustmentType \"%s\" RateType \"%s\" RateCardFeature \"%s\"  was found.\n",
              i++, rateCardCustomizaion.getRateCardId(), rateCardCustomizaion.getId(), rateCardCustomizaion.getStatus(),
              rateCardCustomizaion.getAdjustmentSize(), rateCardCustomizaion.getAdjustmentType(),
              rateCardCustomizaion.getRateType(), rateCardCustomizaion.getRateCardFeature());
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

