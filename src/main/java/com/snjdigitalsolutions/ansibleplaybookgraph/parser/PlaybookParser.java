package com.snjdigitalsolutions.ansibleplaybookgraph.parser;

import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Play;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Task;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlaybookParser {

    private ParsedFileCollector parsedFileCollector;

    public PlaybookParser(ParsedFileCollector parsedFileCollector)
    {
        this.parsedFileCollector = parsedFileCollector;
    }

    public List<Play> parsePlaybook(File playbook, Boolean basePlay)
    {
        List<Play> plays = new ArrayList<Play>();
        if (!parsedFileCollector.hasFileBeenParsed(playbook))
        {
            parsedFileCollector.addParsedFile(playbook);
            try (BufferedReader reader = new BufferedReader(new FileReader(playbook)))
            {
                String inLine = reader.readLine();
                Play currentPlay = null;
                boolean creatingTasks = false;
                while (inLine != null)
                {
                    if (!inLine.startsWith("---")
                            && !inLine.startsWith("#")
                            && !inLine.isEmpty())
                    {
                        if (inLine.startsWith("- name:"))
                        {
                            //create play
                            currentPlay = new Play();
                            currentPlay.setName(inLine.substring("- name:".length()).trim());
                            currentPlay.setFilePath(playbook.getPath());
                            currentPlay.setBasePlay(basePlay);
                            plays.add(currentPlay);
                        }
                        else if (inLine.startsWith("  ansible.builtin.import_playbook:"))
                        {
                            File childPlaybook = new File(playbook.getParentFile(), inLine.split(":")[1].trim());
                            currentPlay.setChildPlays(parsePlaybook(childPlaybook, false));
                        }
                        else if (inLine.startsWith("  hosts:"))
                        {
                            String hosts = inLine.substring("  hosts:".length()).trim();
                            for (String host : hosts.split(" "))
                            {
                                currentPlay.addHost(host.trim());
                            }
                        }
                        else if (inLine.startsWith("  tasks:"))
                        {
                            //create tasks
                            creatingTasks = true;
                            while (inLine != null && !inLine.startsWith("- name:"))
                            {
                                if (inLine.startsWith("    - name:"))
                                {
                                    Task playTask = new Task();
                                    playTask.setName(inLine.split(":")[1].trim());
                                    currentPlay.addTask(playTask);
                                }
                                inLine = reader.readLine();
                            }
                        }
                    }
                    if (creatingTasks)
                    {
                        creatingTasks = false;
                    }
                    else
                    {
                        inLine = reader.readLine();
                    }
                }

            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return plays;
    }

}
