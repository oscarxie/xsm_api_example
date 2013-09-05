package xsm.axis.v201306.ratecardservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201306.StatementBuilder;
import com.google.api.ads.dfp.axis.v201306.RateCard;
import com.google.api.ads.dfp.axis.v201306.RateCardPage;
import com.google.api.ads.dfp.axis.v201306.RateCardServiceInterface;
import com.google.api.ads.dfp.axis.v201306.UpdateResult;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example activate ratecard. To determine which companies exist,
 * run GetAllRateCards.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: RateCardService.deactivateRateCards
 *
 * @author Oscar Xie
 */
public class DeactivateRateCards {

  // Set the ID of the ratecard to update.
  private static final String RATECARD_ID = "84037";

  public static void runExample(DfpServices dfpServices, DfpSession session, long ratecardId)
      throws Exception {
    // Get the RateCardService.
	  RateCardServiceInterface rateCardService =
        dfpServices.get(session, RateCardServiceInterface.class);

    // Create a statement to select a rate card.
    StatementBuilder statementBuilder = new StatementBuilder()
        .where("WHERE id = :id")
        .orderBy("id ASC")
        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
        .withBindVariableValue("id", ratecardId);

    // Default for total result set size.
    int totalResultSetSize = 0;

    do {
      // Get rate cards by statement.
      RateCardPage page = rateCardService.getRateCardsByStatement(statementBuilder.toStatement());

      if (page.getResults() != null) {
        totalResultSetSize = page.getTotalResultSetSize();
        int i = page.getStartIndex();
        for (RateCard rateCard : page.getResults()) {
          System.out.printf(
              "%d) Rate Card with ID \"%d\" will be deactivated.\n", i++, rateCard.getId());
        }
      }

      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
    } while (statementBuilder.getOffset() < totalResultSetSize);

    System.out.printf("Number of rate cards to be deactivated: %d\n", totalResultSetSize);

    if (totalResultSetSize > 0) {
      // Remove limit and offset from statement.
      statementBuilder.removeLimitAndOffset();

      // Create action.
      com.google.api.ads.dfp.axis.v201306.DeactivateRateCards action =
          new com.google.api.ads.dfp.axis.v201306.DeactivateRateCards();

      // Perform action.
      UpdateResult result =
    	  rateCardService.performRateCardAction(action, statementBuilder.toStatement());

      if (result != null && result.getNumChanges() > 0) {
        System.out.printf("Number of rate cards deactivated: %d\n", result.getNumChanges());
      } else {
        System.out.println("No rate cards were deactivated.");
      }
    }
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

    runExample(dfpServices, session, Long.parseLong(RATECARD_ID));
  }
}
