package com.nicovogelaar.kafka.test.connector;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigException;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class HeartRateConnectorConfig extends AbstractConfig {

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to write to";

    public static final String SINCE_CONFIG = "since.timestamp";
    private static final String SINCE_DOC = "Only records after this time are returned.\n"
                    + "This is a timestamp in ISO-8601 format: YYYY-MM-DDTHH:MM:SSZ.\n"
                    + "Defaults to a year from first launch.";

    public HeartRateConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public HeartRateConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, TOPIC_DOC)
                .define(SINCE_CONFIG,
                        Type.STRING,
                        ZonedDateTime.now().minusYears(1).toInstant().toString(),
                        new TimestampValidator(),
                        Importance.HIGH,
                        SINCE_DOC);
    }

    public String getTopic() {
        return this.getString(TOPIC_CONFIG);
    }

    public Instant getSince() {
        return Instant.parse(this.getString(SINCE_CONFIG));
    }

    private static class TimestampValidator implements ConfigDef.Validator {
        @Override
        public void ensureValid(String name, Object value) {
            String timestamp = (String) value;
            try {
                Instant.parse(timestamp);
            } catch (DateTimeParseException e) {
                throw new ConfigException(name, value, "Failed to parse ISO-8601 timestamp");
            }
        }
    }
}
