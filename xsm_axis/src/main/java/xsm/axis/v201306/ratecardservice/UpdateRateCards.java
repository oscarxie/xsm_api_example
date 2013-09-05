package xsm.axis.v201306.ratecardservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201306.RateCard;
import com.google.api.ads.dfp.axis.v201306.RateCardServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example updates ratecard name. To determine which companies exist,
 * run GetAllRateCards.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: RateCardService.updateRateCards
 *
 * @author Oscar Xie
 */
public class UpdateRateCards {

  // Set the ID of the ratecard to update.
  private static final String RATECARD_ID = "84037";

  public static void runExample(DfpServices dfpServices, DfpSession session, long ratecardId)
      throws Exception {
    // Get the RateCardService.
	  RateCardServiceInterface rateCardService =
        dfpServices.get(session, RateCardServiceInterface.class);

    // Get the ratecard.
	  RateCard ratecard = rateCardService.getRateCard(ratecardId);

    // Update the name.
	  ratecard.setName(ratecard.getName() + " Updated.");

    // Update the ratecard on the server.
	  RateCard[] ratecards = rateCardService.updateRateCards(new RateCard[] {ratecard});

    for (RateCard updatedRateCard : ratecards) {
      System.out.printf(
          "RateCard with ID \"%d\", name \"%s\" was updated.\n",
          updatedRateCard.getId(), updatedRateCard.getName());
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
