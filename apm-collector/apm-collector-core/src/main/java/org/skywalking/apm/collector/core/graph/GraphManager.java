/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.collector.core.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Graph 管理器
 *
 * @author wusheng
 */
public enum GraphManager {

    /**
     * 单例
     */
    INSTANCE;

    /**
     * Graph 映射
     * key ：Graph 编号
     */
    private Map<Integer, Graph> allGraphs = new HashMap<>();

    /**
     * 创建 Graph
     *
     * Create a stream process graph.
     *
     * @param graphId represents a graph, which is used for finding it.
     * @return
     */
    public synchronized <INPUT> Graph<INPUT> createIfAbsent(int graphId, Class<INPUT> input) {
        if (!allGraphs.containsKey(graphId)) {
            Graph graph = new Graph(graphId);
            allGraphs.put(graphId, graph);
            return graph;
        } else {
            return allGraphs.get(graphId);
        }
    }

    public Graph findGraph(int graphId) {
        Graph graph = allGraphs.get(graphId);
        if (graph == null) {
            throw new GraphNotFoundException("Graph id=" + graphId + " not found in this GraphManager");
        }
        return graph;
    }

    public void reset() {
        allGraphs.clear();
    }
}
