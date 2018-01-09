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

package org.skywalking.apm.toolkit.activation.trace;

import org.skywalking.apm.agent.core.context.ContextManager;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.skywalking.apm.toolkit.trace.Trace;

import java.lang.reflect.Method;

/**
 * {@link TraceAnnotationMethodInterceptor} create a local span and set the operation name which fetch from
 * <code>org.skywalking.apm.toolkit.trace.annotation.Trace.operationName</code>. if the fetch value is blank string, and
 * the operation name will be the method name.
 *
 * @author zhangxin
 */
public class TraceAnnotationMethodInterceptor implements InstanceMethodsAroundInterceptor {

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        MethodInterceptResult result) throws Throwable {
        // 获得操作名
        Trace trace = method.getAnnotation(Trace.class);
        String operationName = trace.operationName();
        if (operationName.length() == 0) {
            operationName = generateOperationName(method);
        }

        // 创建 LocalSpan 对象
        ContextManager.createLocalSpan(operationName);
    }

    /**
     * 生成方法签名
     *
     * @param method 方法名
     * @return 方法签名
     */
    private String generateOperationName(Method method) {
        StringBuilder operationName = new StringBuilder(method.getDeclaringClass().getName() + "." + method.getName() + "(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            operationName.append(parameterTypes[i].getName());
            if (i < (parameterTypes.length - 1)) {
                operationName.append(",");
            }
        }
        operationName.append(")");
        return operationName.toString();
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes,
        Object ret) throws Throwable {
        // 完成 LocalSpan 对象
        ContextManager.stopSpan();
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().errorOccurred().log(t);
    }
}
