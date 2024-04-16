package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge {

    private String source = null;
    private String target = null;

    public Edge(String source)
    {
        this.source = source;
    }

    public Edge(String source, String target)
    {
        this.source = source;
        this.target = target;
    }


}
