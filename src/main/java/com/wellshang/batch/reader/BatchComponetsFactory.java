package com.wellshang.batch.reader;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;

public class BatchComponetsFactory {
    private static Logger LOG = LoggerFactory.getLogger("BatchComponetsFactory");
    
    public static <T> FieldSetMapper<T> getBeanWrapperFieldSetMapper(Class<? extends T> type) {
        LOG.info("Creating FieldSetMapper...");
        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<T>();
        fieldSetMapper.setTargetType(type);
        
        return fieldSetMapper;
    }
    
    public static <T> LineMapper<T> getDefaultLineMapper(String namesList, FieldSetMapper<T> fieldSetMapper) {
        LOG.info("Creating LineMapper...");
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<T>();

        String[] names = namesList.split("\\,");
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(names);
        
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
    
    public static <T> FlatFileItemReader<T> getFlatFileItemReader(String resourcePath, LineMapper<T> lineMapper) {
        LOG.info("Creating FlatFileItemReader...");
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();
        reader.setResource(new ClassPathResource(resourcePath));
        reader.setLineMapper(lineMapper);
        return reader;
    }
    
    public static <T>JdbcBatchItemWriter<T> getJdbcBatchItemWriter(DataSource dataSource, String sql) {
        LOG.info("Creating JdbcBatchItemWriter...");
        JdbcBatchItemWriter<T> writer = new JdbcBatchItemWriter<T>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<T>());
        writer.setSql(sql);
        writer.setDataSource(dataSource);
        return writer;
    }


}
