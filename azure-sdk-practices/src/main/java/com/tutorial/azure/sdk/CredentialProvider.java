package com.tutorial.azure.sdk;

import com.microsoft.azure.management.Azure;
import com.microsoft.rest.LogLevel;
import java.io.File;

public class CredentialProvider {
    
    public static Azure getLogin() throws Exception {       
        final File credFile = new File("C:\\Users\\admin12345\\projects\\azure-sdk-practices\\src\\main\\java\\com\\tutorial\\azure\\sdk\\azure-credentials.properties");
        
        Azure azureData = Azure.configure()
                    .withLogLevel(LogLevel.BODY)
                    .authenticate(credFile)
                    .withDefaultSubscription();
        
        System.out.println("Azure Selected subscription: " + azureData.subscriptionId());
        
        return azureData;
    }
    
    public static File getDataFile() throws Exception {
        final File dataFile = new File("C:\\Users\\admin12345\\projects\\azure-sdk-practices\\src\\main\\java\\com\\tutorial\\azure\\sdk\\dataFile.txt");

        return dataFile;
    }
}
