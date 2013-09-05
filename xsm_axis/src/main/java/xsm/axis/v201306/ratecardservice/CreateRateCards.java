package xsm.axis.v201306.ratecardservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201306.RateCard;
import com.google.api.ads.dfp.axis.v201306.RateCardServiceInterface;
import com.google.api.ads.dfp.axis.v201306.RateCardStatus;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

import java.util.Random;

/**
 * This example creates new ratecards. To determine which companies exist, run
 * GetAllRateCards.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: RateCardService.CreateRateCards
 *
 * @author Oscar Xie
 */

public class CreateRateCards {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the RateCardService.
	  RateCardServiceInterface rateCardService =
        dfpServices.get(session, RateCardServiceInterface.class);

    // Create an ratecard.
	RateCard ratecard = new RateCard();
	ratecard.setName("RateCard #" + new Random().nextInt(Integer.MAX_VALUE));
	ratecard.setAppliedTeamIds(new long[1]);
	ratecard.setAppliedTeamIds(0, 49117);	// team guowen_xsm
	ratecard.setStatus(RateCardStatus.INACTIVE);

    // Create the ratecards on the server.
    RateCard[] ratecards = rateCardService.createRateCards(new RateCard[] {ratecard});

    for (RateCard createRateCard : ratecards) {
      System.out.printf("A ratecard with ID \"%d\", name \"%s\", and status \"%s\" was created.\n",
          createRateCard.getId(), createRateCard.getName(), createRateCard.getStatus());
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

    runExample(dfpServices, session);
  }
}