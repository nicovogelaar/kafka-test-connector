package com.nicovogelaar.kafka.test.connector;

import com.nicovogelaar.avro.HeartRate;
import io.confluent.connect.avro.AvroData;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartRateSourceTask extends SourceTask {

    private static Logger log = LoggerFactory.getLogger(HeartRateConnector.class);
    private HeartRateConnectorConfig config;
    private AvroData avroData;

    @Override
    public void start(Map<String, String> props) {
        config = new HeartRateConnectorConfig(props);
        avroData = new AvroData(1);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        final ArrayList<SourceRecord> records = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            HeartRate record = new HeartRate("1", 60, Instant.now().getEpochSecond());
            records.add(createSourceRecord(record));
        }

        return records;
    }

    private SourceRecord createSourceRecord(HeartRate record) {
        Schema valueSchema = avroData.toConnectSchema(record.getSchema());
        Struct value = (Struct) avroData.toConnectData(record.getSchema(), record).value();

        return new SourceRecord(
                sourcePartition(record),
                sourceOffset(record),
                config.getTopic(),
                valueSchema,
                value
        );
    }

    private Map<String, String> sourcePartition(HeartRate record) {
        Map<String, String> map = new HashMap<>();
        map.put("personId", record.getPersonId());
        return map;
    }

    private Map<String, String> sourceOffset(HeartRate record) {
        Map<String, String> map = new HashMap<>();
        map.put("time", record.getTime().toString());
        return map;
    }

    @Override
    public void stop() {
    }

    @Override
    public String version() {
        return Version.getVersion();
    }
}
