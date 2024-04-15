package com.snjdigitalsolutions.ansibleplaybookgraph.utility;

import com.snjdigitalsolutions.ansibleplaybookgraph.AbstractTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaybookFileCollectorTest extends AbstractTest {

    @Test
    void fileCollectionTest()
    {
        // Arrange
        File baseDir = new File("src/test/resources");

        // Act
        List<File> playbooksFiles = playbookFileCollector.collectPlaybookFiles(baseDir);

        // Assert
        assertEquals(3, playbooksFiles.size());
        for (File file : playbooksFiles)
        {
            System.out.println(file.getName());
        }
    }


}