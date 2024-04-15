package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Play;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Task;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Setter
public class TemplatedGraphBuilder implements PlayGraphBuilder{

    private List<Play> plays = new ArrayList<>();
    private StringBuilder graphContent = new StringBuilder();


    @Override
    public void buildPlayGraph(File outputFile)
    {
        List<Edge> edges = new ArrayList<>();

        // Front Matter
        addFileContent(new File("src/main/resources/graphFrontMatter"));

        // Graph
        int nodeIndex = 0;
        for (Play play : plays)
        {
            String nodeIndexString = "n" + nodeIndex++;
            String nodeString = getFileContent(new File("src/main/resources/nodeTemplate"));
            nodeString = nodeString.replace(ReplacementString.NODE_INDEX.getString(), nodeIndexString);

            String playLabelBuilder = play.getName() +
                    "\n" +
                    (new File(play.getFilePath()).getName());

            String labelTemplate = getFileContent(new File("src/main/resources/labelTemplate"));
            labelTemplate = labelTemplate.replace(ReplacementString.LABEL_TEXT.getString(), playLabelBuilder);

            nodeString = nodeString.replace(ReplacementString.LABEL_COMMENT.getString(), labelTemplate);

            graphContent.append(nodeString);

            // Add the tasks for a play
            String taskIndexString = null;
            for (Task task : play.getTasks())
            {
                taskIndexString = "n" + nodeIndex++;

                nodeString = getFileContent(new File("src/main/resources/nodeTemplate"));
                nodeString = nodeString.replace(ReplacementString.NODE_INDEX.getString(), taskIndexString);

                labelTemplate = getFileContent(new File("src/main/resources/labelTemplate"));
                labelTemplate = labelTemplate.replace(ReplacementString.LABEL_TEXT.getString(), task.getName());
                nodeString = nodeString.replace(ReplacementString.LABEL_COMMENT.getString(), labelTemplate);
                graphContent.append(nodeString);

                edges.add(new Edge(nodeIndexString, taskIndexString));
            }

        }

        //Add in the Edges
        int edgeIndex = 0;
        for (Edge edge : edges)
        {
            String edgeIndexString = "e" + edgeIndex++;
            String edgeString = getFileContent(new File("src/main/resources/edgeTemplate"));
            edgeString = edgeString.replace(ReplacementString.EDGE_INDEX.getString(), edgeIndexString);
            edgeString = edgeString.replace(ReplacementString.SOURCE_NODE.getString(), edge.getSource());
            edgeString = edgeString.replace(ReplacementString.TARGET_NODE.getString(), edge.getTarget());
            graphContent.append(edgeString);

        }

        //Back matter
        addFileContent(new File("src/main/resources/graphBackMatter"));

        try(FileWriter fileWriter = new FileWriter(outputFile))
        {
            fileWriter.write(graphContent.toString());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void addFileContent(File file)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String inLine = reader.readLine();
            while (inLine != null)
            {
                graphContent.append(inLine).append("\n");
                inLine = reader.readLine();

            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private String getFileContent(File file)
    {
        StringBuilder content = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String inLine = reader.readLine();
            while (inLine != null)
            {
                content.append(inLine).append("\n");;
                inLine = reader.readLine();
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return content.toString();
    }

}
