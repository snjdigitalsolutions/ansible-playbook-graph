package com.snjdigitalsolutions.ansibleplaybookgraph.playbooks;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Play {

    private String name;
    private List<Play> childPlays;
    private final List<String> hosts;
    private final List<Task> tasks;

    public Play()
    {
        this.hosts = new ArrayList<String>();
        this.tasks = new ArrayList<>();
        this.childPlays = new ArrayList<>();
    }

    public void addHost(String host)
    {
        hosts.add(host);
    }

    public void addTask(Task task)
    {
        tasks.add(task);
    }

    public void setChildPLays(List<Play> plays)
    {
        this.childPlays = plays;
    }



}
