package com.snjdigitalsolutions.ansibleplaybookgraph.utility;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PlaybookFileCollector {

    private final List<File> playbookFiles;

    public PlaybookFileCollector()
    {
        this.playbookFiles = new ArrayList<>();
    }

    public List<File> collectPlaybookFiles(File directory)
    {
        if (directory != null && directory.isDirectory())
        {
            for (File file : Objects.requireNonNull(directory.listFiles()))
            {
                if (file.isDirectory())
                {
                    collectPlaybookFiles(file);
                }
                if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"))
                {
                    this.playbookFiles.add(file);
                }
            }
        }
        return this.playbookFiles;
    }

}
