package xsm.axis.v201306.workflowactionservice;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201306.StatementBuilder;
import com.google.api.ads.dfp.axis.v201306.WorkflowAction;
import com.google.api.ads.dfp.axis.v201306.WorkflowActionPage;
import com.google.api.ads.dfp.axis.v201306.WorkflowActionServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;

/**
 * This example gets all workflow actions. To create workflow actions, run
 * CreateWorkflowActions.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: WorkflowactionService.getWorkflowActionsByStatement
 *
 * @author Oscar Xie
 */
public class GetAllWorkflowActions {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the LineItemService.
	  WorkflowActionServiceInterface workflowactionService =
        dfpServices.get(session, WorkflowActionServiceInterface.class);

    // Create a statement to select all line items.
    StatementBuilder statementBuilder = new StatementBuilder()
        .orderBy("id ASC")
        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

    // Default for total result set size.
    int totalResultSetSize = 0;

    do {
      // Get line items by statement.
    	WorkflowActionPage page =
    			workflowactionService.getWorkflowActionsByStatement(statementBuilder.toStatement());

      if (page.getResults() != null) {
        totalResultSetSize = page.getTotalResultSetSize();
        int i = page.getStartIndex();
        for (WorkflowAction workflowAction : page.getResults()) {
          System.out.printf(
              "%d) Workflow Action with ID \"%d\" and name \"%s\" was found.\n", i++,
              workflowAction.getId(), workflowAction.getName());
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
