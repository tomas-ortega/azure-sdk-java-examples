package com.tutorial.azure.sdk;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.azure.management.storage.StorageAccount;
import com.microsoft.azure.management.storage.StorageAccounts;
import com.microsoft.azure.management.storage.StorageService;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.models.ContainerCreateResponse;
import com.microsoft.azure.storage.blob.models.PublicAccessType;
import com.microsoft.rest.v2.Context;
import java.net.URL;
import java.util.List;

public class AzureSDKBlob {
    
    static StorageService storageService = new StorageService();
        
    public static void main(String args[]) throws Exception {
        AzureSDKBlob azureSDK = new AzureSDKBlob();
        Azure azureData = CredentialProvider.getLogin();
        String storageCreatedAccountId;
        StorageAccount storageCreatedAccount;
        
        String resourceGroupName = "sdk-practices";
        String storageAccountName = "storageblob8647";
        
        //storageCreatedAccount = AzureSDKBlob.createStorageAccount(azureData, resourceGroupName, storageAccountName);
        
        //AzureSDKBlob.listStorageAccounts(azureData, resourceGroupName);
        
        //AzureSDKBlob.deleteStorageAccount(azureData, "<ID>");
        
        //AzureSDKBlob.createContainerURL(storageAccountName);
        
        //AzureSDKBlob.uploadFile();
        
        //AzureSDKBlob.downloadFile();
    }
    
    public static StorageAccount createStorageAccount(Azure azureData, 
                                                      String resourceGroupName,
                                                      String storageAccountName) throws Exception {
        
        StorageAccount storageAccount = azureData.storageAccounts().define(storageAccountName)
                    .withRegion(Region.EUROPE_NORTH)
                    .withNewResourceGroup(resourceGroupName)
                    .create();
        
        System.out.println("Created a Storage Account: " + storageAccount.name());
        
        return storageAccount;
    }
    
    public static void deleteStorageAccount(Azure azureData,
                                            String storageAccountId) throws Exception {
        
            azureData.storageAccounts().deleteById(storageAccountId);
            
            System.out.println("Deleted Storage Account by Id: " + storageAccountId);
    }
    
    public static void listStorageAccounts(Azure azureData, String resourceGroupName) throws Exception {
         System.out.println("Listing storage accounts");

            StorageAccounts storageAccounts = azureData.storageAccounts();

            List<StorageAccount> accounts = storageAccounts.listByResourceGroup(resourceGroupName);
            StorageAccount sa;
            for (int i = 0; i < accounts.size(); i++) {
                sa = (StorageAccount) accounts.get(i);
                System.out.println("Storage Account (" + i + ") " + sa.name()
                        + " created @ " + sa.creationTime());
            }
    }
    
    public static void createContainerURL(String storageAccountName) throws Exception {
        ContainerURL containerURL;
        
        // Create a ServiceURL to call the Blob service. We will also use this to construct the ContainerURL
         SharedKeyCredentials creds = new SharedKeyCredentials(storageAccountName, "0sWtPgSv+xCaGu7p82ISLxSME/9gfCAi0dDkGf58qGWpK5XzsoOmknrxfUDILpsdroPAUsAVrxKEGhmxiVvvkQ==");
        
        final ServiceURL serviceURL = new ServiceURL(new URL("https://" + storageAccountName + ".blob.core.windows.net"), 
                StorageURL.createPipeline(creds, new PipelineOptions()));
        
        containerURL = serviceURL.createContainerURL("datacontainer");
        
        ContainerCreateResponse response = containerURL.create(null, PublicAccessType.CONTAINER, Context.NONE).blockingGet();
        
        System.out.println("Container Create Response was " + response.statusCode());
    }
    
    public static void uploadFile() throws Exception {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;
        
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storageblob8647;AccountKey=0sWtPgSv+xCaGu7p82ISLxSME/9gfCAi0dDkGf58qGWpK5XzsoOmknrxfUDILpsdroPAUsAVrxKEGhmxiVvvkQ==;EndpointSuffix=core.windows.net";
        
        // Parse the connection string and create a blob client to interact with Blob storage
        storageAccount = CloudStorageAccount.parse(storageConnectionString);
        blobClient = storageAccount.createCloudBlobClient();
        container = blobClient.getContainerReference("datacontainer");
        
        //Getting a blob reference
	CloudBlockBlob blob = container.getBlockBlobReference(CredentialProvider.getDataFile().getName());
        
        //Creating blob and uploading file to it
        System.out.println("Uploading the sample file ");
        blob.uploadFromFile(CredentialProvider.getDataFile().getAbsolutePath());
    }
    
    public static void downloadFile() throws Exception {
        CloudStorageAccount storageAccount;
        CloudBlobClient blobClient = null;
        CloudBlobContainer container = null;
        
        String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storageblob8647;AccountKey=0sWtPgSv+xCaGu7p82ISLxSME/9gfCAi0dDkGf58qGWpK5XzsoOmknrxfUDILpsdroPAUsAVrxKEGhmxiVvvkQ==;EndpointSuffix=core.windows.net";
        
        // Parse the connection string and create a blob client to interact with Blob storage
        storageAccount = CloudStorageAccount.parse(storageConnectionString);
        blobClient = storageAccount.createCloudBlobClient();
        container = blobClient.getContainerReference("datacontainer");
        
        //Get blob reference
        CloudBlockBlob blob = container.getBlockBlobReference("dataFile.txt");
        
        //Download file
        blob.downloadToFile("C:\\Users\\admin12345\\projects\\azure-sdk-practices\\src\\main\\java\\com\\tutorial\\azure\\sdk\\dataFile2.txt");
    }
}
