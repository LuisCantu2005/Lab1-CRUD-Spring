package com.springboot.MyTodoList.config;


import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;


import javax.sql.DataSource;
import java.sql.SQLException;
///*
//    This class grabs the appropriate values for OracleDataSource,
//    The method that uses env, grabs it from the environment variables set
//    in the docker container. The method that uses dbSettings is for local testing
//    @author: peter.song@oracle.com
// */
//
//
@Configuration
@Profile("oracle")
public class OracleConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(OracleConfiguration.class);

    private final DbSettings dbSettings;
    private final Environment env;

    public OracleConfiguration(DbSettings dbSettings, Environment env) {
        this.dbSettings = dbSettings;
        this.env = env;
    }

    @Bean
    public DataSource dataSource() throws SQLException{
        OracleDataSource ds = new OracleDataSource();
        ds.setDriverType(env.getProperty("driver_class_name"));
        logger.info("Using Driver " + env.getProperty("driver_class_name"));
        ds.setURL(env.getProperty("db_url"));
        logger.info("Using URL: " + env.getProperty("db_url"));
        ds.setUser(env.getProperty("db_user"));
        logger.info("Using Username " + env.getProperty("db_user"));
        ds.setPassword(env.getProperty("dbpassword"));
        return ds;
    }
}
