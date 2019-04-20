package com.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class SheetReader {

    private static final String APPLICATION_NAME = "Shirt Picker";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private Sheets service;
    private final String spreadsheetId = "1XqK9-kRh5HUIs8VYEicJtUolIPMhITVDULjsWdG_-TE";
    private final String range = "Shirts!H2:J";

    SheetReader(){
    }

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetReader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    String[] getShirts(int choices) {

        String [] shirtChoices = new String[choices];
        ValueRange response;
        List<List<Object>> values;
        int counter = 0;

        if (this.service != null) {
            shirtChoices[0] = "hi from new computer. we have some liftoff";
        } else {
            shirtChoices[0] = "hi from new computer. couldn't even start the service.";
        }
        return shirtChoices;
        /*
        try {
            response = this.service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            shirtChoices[0] = "tried again";
            return shirtChoices;

        } catch (IOException e) {
                shirtChoices[0] = "Unsuccessful get";
                return shirtChoices;
        }


        values = response.getValues();
        if (values == null || values.isEmpty()) {
            shirtChoices[0] = "No data found.";
            return shirtChoices;
        } else {

            shirtChoices[0] = "ahhhhh";
            return shirtChoices;
            // For row
            for (List row : values) {

                if (counter <= choices) {

                    // Print columns A and E, which correspond to indices 0 and 4.
                    String shirtName = row.get(0).toString();
                    shirtChoices[counter] = shirtName;

                    shirtChoices[0] = "blah";

                    counter++;

                } else {
                    break;
                }
            }

        }

        return shirtChoices;
    */
    }

    String setUpService() {
        // Build a new authorized API client service.
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            this.service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            return "service setup correctly";
        }
        catch (IOException e) {
            return "IOException";
        } catch (GeneralSecurityException e) {
            return "no dice, security exception";
        }
    }
}
