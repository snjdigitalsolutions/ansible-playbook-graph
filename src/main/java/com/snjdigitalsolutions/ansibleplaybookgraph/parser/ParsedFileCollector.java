package com.snjdigitalsolutions.ansibleplaybookgraph.parser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParsedFileCollector {

    List<File> parsedFiles = new ArrayList<>();

    public boolean hasFileBeenParsed(File fileToCheck)
    {
        return parsedFiles.contains(fileToCheck);
    }

    public void addParsedFile(File parsedFile)
    {
        this.parsedFiles.add(parsedFile);
    }


}
