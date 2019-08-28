package com.tutorial.azure.sdk;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.resources.fluentcore.arm.Region;
import com.microsoft.azure.management.sql.SqlDatabase;
import com.microsoft.azure.management.sql.SqlDatabaseBasicStorage;
import com.microsoft.azure.management.sql.SqlServer;
import java.util.List;

public class AzureSDKSqlServer {
    public static void main(String args[]) throws Exception {
        String resourceGroupName = "sdk-practices";
        String databaseName = "sqldatabasetomy";
        String databaseServerName = "sqlservertomy";
        String sqlServerId = "<SERVER_ID>";
        String sqlDatabaseId = "sqlservertomy";
        
        Azure azureData = CredentialProvider.getLogin();
        
        //AzureSDKSqlServer.createSqlServerDatabase(azureData, resourceGroupName, databaseServerName);
        //AzureSDKSqlServer.listSqlServers(azureData);
        //AzureSDKSqlServer.createDatabase(azureData, sqlServerId, databaseName);
        //AzureSDKSqlServer.listSqlDatabases(azureData, sqlServerId);
        //AzureSDKSqlServer.updateDatabaseType(azureData, sqlServerId, databaseName);
    }
    
    public static void createSqlServerDatabase(Azure azureData, 
                                               String resourceGroupName,
                                               String databaseServer) throws Exception {
        SqlServer sqlServer = azureData.sqlServers().define(databaseServer)
                .withRegion(Region.EUROPE_NORTH)
                .withExistingResourceGroup(resourceGroupName)
                .withAdministratorLogin("admin12345")
                .withAdministratorPassword("myS3cureP@ssword")
                .create();
        
        System.out.println("Created SQL Server: " + databaseServer);
    }
    
    public static void listSqlServers(Azure azureData) throws Exception {
        System.out.println("List Sql Servers");
        System.out.println("=================");
        
        azureData.sqlServers()
                            .list().forEach((singleSqlServer) -> {
                                System.out.println("SQL Server ID: " + singleSqlServer.id());
                                System.out.println("SQL Server Name: " + singleSqlServer.name());
                            });
    }
    
    public static void createDatabase(Azure azureData,
                                      String sqlServerId,
                                      String databaseName) throws Exception {
        
        SqlDatabase sqlDatabase = azureData.sqlServers()
                                       .getById(sqlServerId)
                                       .databases()
                                       .define(databaseName)
                                       .create();
        
        System.out.println("Database " + databaseName + " Created!");
    }
    
    public static void listSqlDatabases(Azure azureData,
                                        String sqlServerId) throws Exception {
        System.out.println("Listing Databases From Server: " + sqlServerId);
        System.out.println("=============================================");
        
        SqlServer sqlServer = azureData.sqlServers().getById(sqlServerId);
        
        sqlServer.databases().list().forEach((singleDatabase) -> {
            System.out.println("SQL Database ID: " + singleDatabase.databaseId());
            System.out.println("SQL Database Name: " + singleDatabase.name());
        });
    }
    
    public static void updateDatabaseType(Azure azureData,
                                          String sqlServerId,
                                          String sqlDatabaseName) throws Exception {
        System.out.println("Updating size database to 100MB Storage & basic edition");
        System.out.println("=======================================================");
        
                  List<SqlDatabase>  databaseToUpdate = azureData.sqlServers()
                                            .getById(sqlServerId)
                                            .databases()
                                            .list();
                                            
                    databaseToUpdate.forEach((singleDatabase) -> {                       
                        if (singleDatabase.name().equals(sqlDatabaseName)) {
                            singleDatabase
                                    .update()
                                    .withBasicEdition(SqlDatabaseBasicStorage.MAX_100_MB)
                                    .apply();
                            
                            System.out.println("Database Updated: " + singleDatabase.name());
                        }
                    });
    }
}
