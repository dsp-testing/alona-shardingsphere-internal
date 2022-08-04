/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.shadow.yaml.swapper;

import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapper;
import org.apache.shardingsphere.infra.yaml.config.swapper.algorithm.AlgorithmConfigurationYamlSwapper;
import org.apache.shardingsphere.shadow.api.config.ShadowRuleConfiguration;
import org.apache.shardingsphere.shadow.constant.ShadowOrder;
import org.apache.shardingsphere.shadow.yaml.config.YamlShadowRuleConfiguration;
import org.apache.shardingsphere.shadow.yaml.swapper.datasource.ShadowDataSourceConfigurationYamlSwapper;
import org.apache.shardingsphere.shadow.yaml.swapper.table.ShadowTableConfigurationYamlSwapper;

/**
 * Shadow rule configuration yaml swapper.
 */
public final class ShadowRuleConfigurationYamlSwapper implements YamlRuleConfigurationSwapper<YamlShadowRuleConfiguration, ShadowRuleConfiguration> {
    
    private final ShadowDataSourceConfigurationYamlSwapper dataSourceConfigurationSwapper = new ShadowDataSourceConfigurationYamlSwapper();
    
    private final ShadowTableConfigurationYamlSwapper tableConfigurationYamlSwapper = new ShadowTableConfigurationYamlSwapper();
    
    private final AlgorithmConfigurationYamlSwapper algorithmSwapper = new AlgorithmConfigurationYamlSwapper();
    
    @Override
    public YamlShadowRuleConfiguration swapToYamlConfiguration(final ShadowRuleConfiguration data) {
        YamlShadowRuleConfiguration result = new YamlShadowRuleConfiguration();
        result.setDefaultShadowAlgorithmName(data.getDefaultShadowAlgorithmName());
        parseDataSources(data, result);
        parseShadowTables(data, result);
        parseShadowAlgorithms(data, result);
        return result;
    }
    
    private void parseShadowAlgorithms(final ShadowRuleConfiguration data, final YamlShadowRuleConfiguration yamlConfig) {
        data.getShadowAlgorithms().forEach((key, value) -> yamlConfig.getShadowAlgorithms().put(key, algorithmSwapper.swapToYamlConfiguration(value)));
    }
    
    private void parseShadowTables(final ShadowRuleConfiguration data, final YamlShadowRuleConfiguration yamlConfig) {
        data.getTables().forEach((key, value) -> yamlConfig.getTables().put(key, tableConfigurationYamlSwapper.swapToYamlConfiguration(value)));
    }
    
    private void parseDataSources(final ShadowRuleConfiguration data, final YamlShadowRuleConfiguration yamlConfig) {
        data.getDataSources().forEach((key, value) -> yamlConfig.getDataSources().put(key, dataSourceConfigurationSwapper.swapToYamlConfiguration(value)));
    }
    
    @Override
    public ShadowRuleConfiguration swapToObject(final YamlShadowRuleConfiguration yamlConfig) {
        ShadowRuleConfiguration result = new ShadowRuleConfiguration();
        result.setDefaultShadowAlgorithmName(yamlConfig.getDefaultShadowAlgorithmName());
        parseYamlDataSources(yamlConfig, result);
        parseYamlShadowTables(yamlConfig, result);
        parseYamlShadowAlgorithms(yamlConfig, result);
        return result;
    }
    
    private void parseYamlShadowAlgorithms(final YamlShadowRuleConfiguration yamlConfig, final ShadowRuleConfiguration data) {
        yamlConfig.getShadowAlgorithms().forEach((key, value) -> data.getShadowAlgorithms().put(key, algorithmSwapper.swapToObject(value)));
    }
    
    private void parseYamlShadowTables(final YamlShadowRuleConfiguration yamlConfig, final ShadowRuleConfiguration data) {
        yamlConfig.getTables().forEach((key, value) -> data.getTables().put(key, tableConfigurationYamlSwapper.swapToObject(value)));
    }
    
    private void parseYamlDataSources(final YamlShadowRuleConfiguration yamlConfig, final ShadowRuleConfiguration data) {
        yamlConfig.getDataSources().forEach((key, value) -> data.getDataSources().put(key, dataSourceConfigurationSwapper.swapToObject(value)));
    }
    
    @Override
    public Class<ShadowRuleConfiguration> getTypeClass() {
        return ShadowRuleConfiguration.class;
    }
    
    @Override
    public String getRuleTagName() {
        return "SHADOW";
    }
    
    @Override
    public int getOrder() {
        return ShadowOrder.ORDER;
    }
}
