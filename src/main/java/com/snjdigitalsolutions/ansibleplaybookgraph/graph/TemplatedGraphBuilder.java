package com.snjdigitalsolutions.ansibleplaybookgraph.graph;

import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Play;
import com.snjdigitalsolutions.ansibleplaybookgraph.playbooks.Task;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
@Setter
public class TemplatedGraphBuilder implements PlayGraphBuilder{

    private List<Play> plays = new ArrayList<>();
    private StringBuilder graphContent = new StringBuilder();
    private List<Edge> edges = new ArrayList<>();
    private int nodeIndex = 0;


    @Override
    public void buildPlayGraph(File outputFile)
    {

        // Front Matter
        addFileContent(new File("src/main/resources/graphFrontMatter"));

        // Nodes
        for (Play play : plays)
        {
            addPlayToGraph(play,Optional.empty());
        }

        // Edges
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

        // Create parent node for plays from same file
        Map<String,List<Play>> playMap = new HashMap<>();
        for (Play play : plays)
        {
            if (!playMap.containsKey(play.getFilePath()))
            {
                playMap.put(play.getFilePath(),new ArrayList<>());
            }
            playMap.get(play.getFilePath()).add(play);
        }
        for (String key : playMap.keySet())
        {
            if (playMap.get(key).size() >= 2)
            {
                String nodeIndexString = "n" + nodeIndex++;
                String nodeString = getFileContent(new File("src/main/resources/nodeTemplate"));
                nodeString = nodeString.replace(ReplacementString.NODE_INDEX.getString(), nodeIndexString);
                String labelTemplate = getFileContent(new File("src/main/resources/labelTemplate"));
                labelTemplate = labelTemplate.replace(ReplacementString.LABEL_TEXT.getString(), "File\n" + (new File(key)).getName());
                nodeString = nodeString.replace(ReplacementString.LABEL_COMMENT.getString(), labelTemplate);
                graphContent.append(nodeString);

                for (Play play : playMap.get(key))
                {
                    String edgeIndexString = "e" + edgeIndex++;
                    String edgeString = getFileContent(new File("src/main/resources/edgeTemplate"));
                    edgeString = edgeString.replace(ReplacementString.EDGE_INDEX.getString(), edgeIndexString);
                    edgeString = edgeString.replace(ReplacementString.SOURCE_NODE.getString(),nodeIndexString);
                    edgeString = edgeString.replace(ReplacementString.TARGET_NODE.getString(), play.getNodeID());
                    graphContent.append(edgeString);
                }

            }
        }

        // Back matter
        addFileContent(new File("src/main/resources/graphBackMatter"));

        try(FileWriter fileWriter = new FileWriter(outputFile))
        {
            fileWriter.write(graphContent.toString());
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void addPlayToGraph(Play play, Optional<Edge> edge)
    {
        String nodeIndexString = "n" + nodeIndex++;
        play.setNodeID(nodeIndexString);
        if (edge.isPresent())
        {
            edge.get().setTarget(nodeIndexString);
            edges.add(edge.get());
        }

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
        for (Task task : play.getTasks())
        {
            addTaskToGraph(task, nodeIndexString);
        }

        for (Play childPlay : play.getChildPlays())
        {
            Optional<Edge> childEdge = Optional.of(new Edge(nodeIndexString));
            addPlayToGraph(childPlay, childEdge);
        }
    }

    private void addTaskToGraph(Task task, String nodeIndexString)
    {
        String taskIndexString = "n" + nodeIndex++;

        String nodeString = getFileContent(new File("src/main/resources/nodeTemplate"));
        nodeString = nodeString.replace(ReplacementString.NODE_INDEX.getString(), taskIndexString);

        String labelTemplate = getFileContent(new File("src/main/resources/labelTemplate"));
        labelTemplate = labelTemplate.replace(ReplacementString.LABEL_TEXT.getString(), task.getName());
        nodeString = nodeString.replace(ReplacementString.LABEL_COMMENT.getString(), labelTemplate);
        graphContent.append(nodeString);

        edges.add(new Edge(nodeIndexString, taskIndexString));
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
