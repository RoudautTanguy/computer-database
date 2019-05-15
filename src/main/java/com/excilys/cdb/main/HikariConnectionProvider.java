package com.excilys.cdb.main;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class HikariConnectionProvider {

	private HikariConfig config = new HikariConfig("/config.properties");
    private HikariDataSource ds = new HikariDataSource( config );
    
    public HikariDataSource getDs() {
    	return this.ds;
    }
}
