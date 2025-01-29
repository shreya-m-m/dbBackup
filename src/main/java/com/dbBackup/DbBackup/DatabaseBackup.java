package com.dbBackup.DbBackup;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


public class DatabaseBackup {


    public static void backUp() {
    	 
    	// Load database credentials and backup directory from application.properties
    	Properties properties = new Properties();
    	try (InputStream input = DatabaseBackup.class.getClassLoader().getResourceAsStream("application.properties")) {
    	    if (input == null) {
    	        System.err.println("Error: application.properties not found in classpath.");
    	        return;
    	    }
    	    properties.load(input);
    	    System.out.println("Properties loaded successfully.");
    	} catch (IOException ex) {
    	    System.err.println("Error loading configuration file: " + ex.getMessage());
    	    return;
    	}


        // Retrieve credentials and backup directory from the properties file
        String dburl =  properties.getProperty("DB_URL");
    	String dbUser = properties.getProperty("DB_USER");
        System.out.println("Printing dbUser "+dbUser);
        String dbPassword = properties.getProperty("DB_PASSWORD");
        System.out.println("Printing dbPassword "+dbPassword);
        String dbName = properties.getProperty("DB_NAME");  
        String outputDir = properties.getProperty("BACKUP_DIR"); 

        // Check if the properties are valid
        try (Connection connection = DriverManager.getConnection(dburl, dbUser, dbPassword)) {
            System.out.println("Connected to the database successfully!");

            // Generate a backup file name based on current date
            String backupFileName = dbName+"_backup_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".sql";
            String backupFilePath = outputDir + "/" + backupFileName;

            // Construct the mysqldump command
            String command = String.format("mysqldump -u %s -p%s --quick --single-transaction %s > %s",
                    dbUser, dbPassword, dbName, backupFilePath);

            // Execute the mysqldump command
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd", "/c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Backup successfully created: " + backupFilePath);
            } else {
                System.out.println("Backup failed with exit code: " + exitCode);
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during backup: " + e.getMessage());
            e.printStackTrace();
        }

       

    }
}
