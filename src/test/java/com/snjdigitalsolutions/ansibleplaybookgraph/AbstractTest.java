package com.snjdigitalsolutions.ansibleplaybookgraph;

import com.snjdigitalsolutions.ansibleplaybookgraph.parser.PlaybookParser;
import com.snjdigitalsolutions.ansibleplaybookgraph.utility.PlaybookFileCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractTest {

    @Autowired
    protected PlaybookFileCollector playbookFileCollector;
    @Autowired
    protected PlaybookParser playbookParser;

}
