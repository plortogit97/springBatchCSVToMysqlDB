package com.voiture.etatvoiture.listener;

import com.voiture.etatvoiture.Voiture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! Job Fini");

            List<Voiture> results = jdbcTemplate.query("SELECT marque, modele, etat FROM voitures", new RowMapper<Voiture>() {
                @Override
                public Voiture mapRow(ResultSet rs, int row) throws SQLException {
                    return new Voiture(rs.getString(1), rs.getString(2),rs.getString(3));
                }
            });

            for (Voiture voiture : results) {
                log.info("Trouvé <" + voiture + "> dans la base.");
            }

        }
    }
}
