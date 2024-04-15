package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Edge {

    private final String source;
    private final String target;

    public Edge(String source, String target)
    {
        this.source = source;
        this.target = target;
    }


}
