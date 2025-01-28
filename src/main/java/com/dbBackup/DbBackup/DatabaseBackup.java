package com.dbBackup.DbBackup;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class DatabaseBackup {


    public static void backUp() {
    	  // Load database credentials and backup directory from application.properties
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading configuration file: " + ex.getMessage());
            return;
        }

        // Retrieve credentials and backup directory from the properties file
        String dbUser = properties.getProperty("DB_USER");
        String dbPassword = properties.getProperty("DB_PASSWORD");
        String dbName = properties.getProperty("DB_NAME");  
        String outputDir = properties.getProperty("BACKUP_DIR"); 

        // Check if the properties are valid
        if (dbUser == null || dbPassword == null) {
            System.out.println("Error: Database credentials not set in the properties file.");
            return;
        }

        // Generate a backup file name based on current date
        String backupFileName = dbName+"backup_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".sql";
        String backupFilePath = outputDir + "/" + backupFileName;

        // Construct the mysqldump command
        String command = String.format("mysqldump -u %s -p%s --quick --single-transaction %s > %s",
                dbUser, dbPassword, dbName, backupFilePath);

        // Execute the mysqldump command
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd", "/c", command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup successfully created: " + backupFilePath);
            } else {
                System.out.println("Backup failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during backup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
