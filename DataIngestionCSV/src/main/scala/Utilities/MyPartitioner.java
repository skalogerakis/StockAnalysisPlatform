package Utilities;


import com.google.protobuf.InvalidProtocolBufferException;
import grpc.modules.Batch;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;


import java.util.List;
import java.util.Map;

public class MyPartitioner implements Partitioner {

    private static final int BATCH_SIZE = 5;
    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        Batch val1 = null;
        try {
            val1 = Batch.parseFrom(valueBytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("RunError");
        }


        // Other records will go to the rest of the Partitions using a hashing function
        long id = val1.getSeqId();
        int batch = (int) (id / BATCH_SIZE);
        int partition = batch % numPartitions;

        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}

