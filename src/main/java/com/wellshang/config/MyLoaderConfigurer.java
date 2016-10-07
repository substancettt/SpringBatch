package com.wellshang.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.wellshang.batch.listener.BatchJobExecutionListener;
import com.wellshang.batch.processors.PersonItemProcessor;
import com.wellshang.batch.reader.BatchComponetsFactory;
import com.wellshang.models.Person;

@SpringBootApplication(scanBasePackages = {"com.wellshang.batch.*"} )
@EnableBatchProcessing
@Import({ DatabaseConfigurer.class })
public class MyLoaderConfigurer {

    protected Logger LOG = LoggerFactory.getLogger("BatchConfiguration");

    @Value("${batch.loader1.resourcePath}")
    private String resourcePath;

    @Value("${batch.loader1.names}")
    private String namesList;

    @Value("${batch.loader1.sql}")
    private String sql;

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Bean
    public FieldSetMapper<Person> fieldSetMapper() {
        return BatchComponetsFactory.getBeanWrapperFieldSetMapper(Person.class);
    }

    @Bean
    @StepScope
    public LineMapper<Person> lineMapper() {
        return BatchComponetsFactory.getDefaultLineMapper(namesList, fieldSetMapper());
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Person> reader() {
        return BatchComponetsFactory.getFlatFileItemReader(resourcePath, lineMapper());
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer() {
        return BatchComponetsFactory.getJdbcBatchItemWriter(dataSource, sql);
    }

    @Bean
    public BatchJobExecutionListener listener() {
        return new BatchJobExecutionListener();
    }

    // Job
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step())
                .end()
                .build();
    }

    // Step
    @Bean
    public Step step() {
        return stepBuilderFactory.get("myLoader")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
