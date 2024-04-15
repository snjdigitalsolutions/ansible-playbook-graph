package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import com.snjdigitalsolutions.ansibleplaybookgraph.AbstractTest;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Play;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemplatedGraphBuilderTest extends AbstractTest {

    @Test
    void createGraph()
    {
        // Arrange
        File baseDirectory = new File("src/test/resources/exampledir");
        List<File> collectedFiles = playbookFileCollector.collectPlaybookFiles(baseDirectory);
        List<Play> plays = new ArrayList<>();
        for (File file : collectedFiles)
        {
            plays.addAll(playbookParser.parsePlaybook(file, true));
        }
        (new File("/opt/ansible")).mkdirs();
        File outputFile = new File("/opt/ansible/graph.graphml");

        // Act
        templatedGraphBuilder.setPlays(plays);
        templatedGraphBuilder.buildPlayGraph(outputFile);

        // Assert
        assertTrue(outputFile.exists());
    }

}