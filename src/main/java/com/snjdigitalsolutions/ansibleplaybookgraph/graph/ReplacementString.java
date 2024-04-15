package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import lombok.Getter;

@Getter
public enum ReplacementString {

    NODE_INDEX("%nodeindex%"),
    LABEL_COMMENT("<!--label-->"),
    LABEL_TEXT("%LabelText%"),
    EDGE_INDEX("%egdeindex%"),
    SOURCE_NODE("%sourcenode%"),
    TARGET_NODE("%targetnode%");

    private final String string;

    ReplacementString(String string)
    {
        this.string = string;
    }

}
