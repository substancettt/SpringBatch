package com.wellshang.batch.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.wellshang.models.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    
    private Logger LOG = LoggerFactory.getLogger("PersonItemProcessor");
    
    @Override
    public Person process(Person item) throws Exception {
        final String firstName = item.getFirstName().toUpperCase();
        final String lastName = item.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName);

        LOG.info("Converting (" + item + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}
