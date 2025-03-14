package io.jotnkode.siguranj;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.driverClassName("org.sqlite.JDBC");
        builder.url("jdbc:sqlite:siguranj.db");
        return builder.build();
    }
}
