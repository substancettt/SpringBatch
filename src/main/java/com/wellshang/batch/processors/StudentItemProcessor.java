package com.wellshang.batch.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.wellshang.models.Student;

public class StudentItemProcessor implements ItemProcessor<Student, Student> {

    private Logger LOG = LoggerFactory.getLogger("StudentItemProcessor");

    @Override
    public Student process(final Student student) throws Exception {
        int totalMark = student.getSubMarkOne() + student.getSubMarkTwo();
        student.setTotalSubMark(totalMark);
        LOG.info("Student id: " + student.getStdId());
        LOG.info("Student subMarkOne: " + student.getSubMarkOne() + ", subMarkTwo: " + student.getSubMarkTwo());
        LOG.info("Student totalSubMark: " + student.getTotalSubMark());
        return student;
    }

}
