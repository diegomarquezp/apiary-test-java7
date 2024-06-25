package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.Storage.Objects.Insert;
import com.google.api.services.storage.model.StorageObject;
// import com.google.auth.http.HttpCredentialsAdapter;
// import com.google.auth.oauth2.GoogleCredentials;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  public static void main(String[] args) throws Exception {
    Storage supportService = new Storage.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(),
        // new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault())
        null
    )
        .setApplicationName("Cloud Support API")
        .build();

    Logger logger = Logger.getLogger(HttpTransport.class.getName());
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.FINE);
    logger.addHandler(consoleHandler);

    // file to upload
    java.net.URL resource = Main.class.getResource("/SOME_FILE.txt");
    if (resource == null) {
      throw new Exception("resource not found");
    }
    String filepath = resource.getFile();
    File file = new File(filepath);

    // perform upload
    logger.setLevel(Level.FINE);
    StorageObject object = new StorageObject();
    object.setName("name1");

    // direct upload
    System.out.println("**************** direct upload ****************");
    Insert directrequest = supportService.objects()
        .insert("special-bucket-1",
            object,
            new FileContent("text/plain", file));
    directrequest.getMediaHttpUploader().setDirectUploadEnabled(true);
    directrequest.setUploadType("media");

    System.out.println(directrequest.execute());
  }

}
