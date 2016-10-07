package com.wellshang.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
import com.wellshang.batch.processors.StudentItemProcessor;
import com.wellshang.batch.reader.BatchComponetsFactory;
import com.wellshang.models.Student;

@SpringBootApplication(scanBasePackages = {"com.wellshang.batch.*"} )
@EnableBatchProcessing
@Import({ DatabaseConfigurer.class })
public class StudentLoaderConfigurer {

    protected Logger LOG = LoggerFactory.getLogger("BatchConfiguration");

    @Value("${batch.loader2.resourcePath}")
    private String resourcePath;

    @Value("${batch.loader2.names}")
    private String namesList;

    @Value("${batch.loader2.sql}")
    private String sql;

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Bean
    public FieldSetMapper<Student> fieldSetMapper() {
        return BatchComponetsFactory.getBeanWrapperFieldSetMapper(Student.class);
    }

    @Bean
    public LineMapper<Student> lineMapper() {
        return BatchComponetsFactory.getDefaultLineMapper(namesList, fieldSetMapper());
    }

    @Bean
    public FlatFileItemReader<Student> reader() {
        return BatchComponetsFactory.getFlatFileItemReader(resourcePath, lineMapper());
    }

    @Bean
    public StudentItemProcessor processor() {
        return new StudentItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Student> writer() {
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
                .<Student, Student> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
