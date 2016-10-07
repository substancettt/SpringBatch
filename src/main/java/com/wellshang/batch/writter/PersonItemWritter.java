package com.wellshang.batch.writter;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import com.wellshang.models.Person;

public class PersonItemWritter implements ItemWriter<Person> {
    private Logger LOG = LoggerFactory.getLogger("PersonItemProcessor");
    
    private JdbcBatchItemWriter<Person> writer;
    
    public PersonItemWritter(DataSource dataSource) {
        writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
        writer.setDataSource(dataSource);
    }
    
    @Override
    public void write(List<? extends Person> items) throws Exception {
        LOG.info("writing items...");
        writer.write(items);
    }
}