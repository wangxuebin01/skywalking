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

package org.skywalking.apm.network.trace.component;

/**
 * 组件接口
 *
 * The <code>Component</code> represents component library,
 * which has been supported by skywalking sniffer.
 *
 * The supported list is in {@link ComponentsDefine}.
 *
 * @author wusheng
 */
public interface Component {

    /**
     * @return 编号
     */
    int getId();

    /**
     * @return 组件名
     */
    String getName();

}
