package com.snjdigitalsolutions.ansibleplaybookgraph.parser;

import com.snjdigitalsolutions.ansibleplaybookgraph.AbstractTest;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Play;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaybookParserTest extends AbstractTest {

    @Test
    void testBasePlayParse()
    {
        // Arrange
        File basePlaybook = new File("src/test/resources/exampledir/baseplay.yaml");

        // Act
        List<Play> plays = playbookParser.parsePlaybook(basePlaybook, true);

        // Assert
        assertEquals(2, plays.size());
        for (Play play : plays)
        {
            System.out.println(play.getName());
            if (play.getName().equals("Base Play"))
            {
                assertEquals(2, play.getTasks().size());
                for (Task task : play.getTasks())
                {
                    System.out.println(task.getName());
                }
            }
            else if (play.getName().equals("Perform Dir 1"))
            {
                assertEquals(1, play.getChildPlays().size());
                assertEquals(2, play.getChildPlays().get(0).getTasks().size());
                for (Task task : play.getChildPlays().get(0).getTasks())
                {
                    System.out.println(task.getName());
                }
            }
        }
    }

    @Test
    void testDirectoryParse()
    {
        // Arrange
        File baseDirectory = new File("src/test/resources/exampledir");
        List<File> collectedFiles = playbookFileCollector.collectPlaybookFiles(baseDirectory);
        List<Play> plays = new ArrayList<>();

        // Act
        for (File file : collectedFiles)
        {
            plays.addAll(playbookParser.parsePlaybook(file, true));
        }

        // Assert
        assertEquals(3, plays.size());
    }

}