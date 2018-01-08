package com.yq.stream.agent;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SelectAgent {

    public static void main(String[] args) {
        ArrayList<AgentProperty> agentsList = new ArrayList<> ();
        for(int i=0; i < 500; i++) {
            UUID id = UUID.randomUUID();
            String name = "name" + i;
            String desc = "desc" + i + id.toString();
            AgentProperty agentProp = new AgentProperty(id, name, desc, 3);

            if (i % 2 == 0) {
                ArrayList<String> tagsList = new ArrayList<>();
                tagsList.add("Linux");
                agentProp.addTags(tagsList);
            }

            if (i % 5 == 0) {
                ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();
                properties.put("prop1", "value1");
                properties.put("prop2", "value2");
                properties.put("prop3", "value4");
                agentProp.addAgentProperties(properties);
            }

            if (i % 3 == 0) {
                ArrayList<String> tagsList = new ArrayList<>();
                tagsList.add("JDK7");
                tagsList.add("MAVEN3");
                agentProp.addTags(tagsList);
            }
            agentsList.add(agentProp);
        }

        AgentProperty agentProp100 = agentsList.get(100);
        agentProp100.setCurrentJob(3);
        ArrayList<String> tagsList = new ArrayList<>();
        tagsList.add("GO1.7");
        agentProp100.addTags(tagsList);

        AgentProperty agentProp200 = agentsList.get(200);
        agentProp200.setCurrentJob(3);
        ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();
        properties.put("version", "10");
        agentProp200.addAgentProperties(properties);
        ArrayList<String> tagsList2 = new ArrayList<>();
        tagsList2.add("MAVEN3");
        agentProp200.addTags(tagsList2);

        AgentProperty agentProp299 = agentsList.get(299);
        ConcurrentHashMap<String, String> propertiesVersion8 = new ConcurrentHashMap<>();
        propertiesVersion8.put("version", "8");
        agentProp299.addAgentProperties(propertiesVersion8);

        AgentProperty agentProp300 = agentsList.get(300);
        agentProp300.addAgentProperties(propertiesVersion8);


        Stream<AgentProperty> agentStream = agentsList.stream();
        long current = System.currentTimeMillis();
        Optional<AgentProperty> agentRunning3Jobs = agentStream.filter(s -> (s.getCurrentJob() == 3) ).findAny();
        long after = System.currentTimeMillis();
        String agentName = "";
        if (agentRunning3Jobs.isPresent()) {
            agentName = agentRunning3Jobs.get().getName();
        }
        else {
            agentName = "not found";
        }
        System.out.println("当前运行job数为3的agent: " + agentName + ", used time:" + (after - current));

        findAgentByTag(agentsList);

        findAgentByTagAndProperty(agentsList);

        System.out.println("End");
    }
    
    static void findAgentByTag(ArrayList<AgentProperty> agentsList) {
        Stream<AgentProperty> agentStream = agentsList.stream();
        long current = System.currentTimeMillis();
        Optional<AgentProperty> agentContainsTag =
            agentStream.filter(s -> (
                s.getTags().contains("MAVEN3")
            ) ).findAny();
        long after = System.currentTimeMillis();
        String agentName = "";
        if (agentContainsTag.isPresent()) {
            agentName = agentContainsTag.get().getName();
        }
        else {
            agentName = "not found";
        }
        System.out.println("包含 tag 'MAVEN3' 的agent: " + agentName + ", used time:" + (after - current));
    }

    static void findAgentByTagAndProperty(ArrayList<AgentProperty> agentsList) {
        Stream<AgentProperty> agentStream = agentsList.parallelStream();
        long current = System.currentTimeMillis();
        Optional<AgentProperty> agentContainsTag =
            agentStream.filter(s -> (
                s.getTags().contains("Linux") &&
                s.getAgentProperties().containsKey("version") &&
                Integer.valueOf(s.getAgentProperties().get("version")) > 9
            ) ).findAny();
        long after = System.currentTimeMillis();
        String agentName = "";
        if (agentContainsTag.isPresent()) {
            agentName = agentContainsTag.get().getName();
        }
        else {
            agentName = "not found";
        }
        System.out.println("包含 tag 'Linux' 并且 'Version'大于 9的agent: " + agentName + ", used time:" + (after - current));
    }
}
