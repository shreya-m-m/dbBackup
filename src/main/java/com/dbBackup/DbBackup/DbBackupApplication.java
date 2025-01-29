package com.dbBackup.DbBackup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbBackupApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbBackupApplication.class, args);
		
		DatabaseBackup.backUp();
		
	}

}
