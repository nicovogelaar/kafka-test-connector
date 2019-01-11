package com.nicovogelaar.kafka.test.connector;

import com.nicovogelaar.avro.HeartRate;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final public class HeartRateConnector extends SourceConnector {

    private static Logger log = LoggerFactory.getLogger(HeartRateConnector.class);
    private HeartRateConnectorConfig config;

    @Override
    public void start(Map<String, String> props) {
        config = new HeartRateConnectorConfig(props);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return HeartRateSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>(1);
        configs.add(config.originalsStrings());
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        new HeartRate();
        return HeartRateConnectorConfig.conf();
    }

    @Override
    public String version() {
        return Version.getVersion();
    }
}
