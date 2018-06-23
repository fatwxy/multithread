package com.yq.stream.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AgentProperty 主要是共agentSelector选择agent使用， 它定义了agent的主要属性，也就是大部分是key/value
 * 这些key/value既包含agent从host上收集的环境变化和基本属性（OS name, version等信息），还包括自定义的属性
 * 以及我们内置自的属性，例如agent maxJob， currentJob等信息
 * @author YangQian
 *
 */
public class AgentProperty {
    private int maxJob = 3;
    private int currentJob = 0;
    private UUID id = null;
    private String name = "";
    private String description = "";
    private ConcurrentHashMap<String, String> propsMap = new ConcurrentHashMap<>();
    private ArrayList<String> tagsList = new ArrayList<>();

    AgentProperty(UUID id, String name, String description, int maxJob) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maxJob = 5;
    }

    public UUID getId() {
        return id;
    }

    public int getMaxJob() {
        return maxJob;
    }

    public void setMaxJob(int maxJob) {
        this.maxJob = maxJob;
    }

    public int getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(int currentJob) {
        this.currentJob = currentJob;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addTags(List<String> tags) {
        this.tagsList.addAll(tags);
    }

    public void addAgentProperties(Map<String, String> properties) {
        this.propsMap.putAll(properties);
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tagsList);
    }

    public Map<String, String> getAgentProperties() {
        return Collections.unmodifiableMap(propsMap);
    }
}
