package com.voiture.etatvoiture.config;

import com.voiture.etatvoiture.Voiture;

import com.voiture.etatvoiture.listener.JobCompletionNotificationListener;
import com.voiture.etatvoiture.processor.VoitureItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Voiture> reader() {
        FlatFileItemReader<Voiture> reader = new FlatFileItemReader<Voiture>();
        reader.setResource(new ClassPathResource("voitures.csv"));
        reader.setLineMapper(new DefaultLineMapper<Voiture>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "marque", "modele","etat" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Voiture>() {{
                setTargetType(Voiture.class);
            }});
        }});
        return reader;
    }

    @Bean
    public VoitureItemProcessor processor() {
        return new VoitureItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Voiture> writer() {
        JdbcBatchItemWriter<Voiture> writer = new JdbcBatchItemWriter<Voiture>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Voiture>());
        writer.setSql("INSERT INTO voitures (marque,modele,etat) VALUES (:marque, :modele,:etat)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Voiture, Voiture> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
