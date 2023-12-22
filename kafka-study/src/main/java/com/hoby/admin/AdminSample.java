package com.hoby.admin;

import com.hoby.constant.Constants;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;

/**
 * @author hoby
 * @since 2023-10-29
 */
public class AdminSample {

    public static void main(String[] args) throws Exception {
        // AdminClient adminClient = AdminSample.adminClient();
        // System.out.println("adminClient = " + adminClient);
        // createTopics();
        // deleteTopics();
        // listTopics();
        describeTopics();
        // describeConfig();
        // alterConfig();
        // createPartitions();
    }

    /**
     * 增加 partition 数量
     */
    public static void createPartitions() throws Exception {
        AdminClient adminClient = adminClient();
        HashMap<String, NewPartitions> newPartitions = new HashMap<>();
        newPartitions.put(Constants.TOPIC_NAME, NewPartitions.increaseTo(2));
        CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(newPartitions);
        createPartitionsResult.all().get();
    }

    /**
     * 修改配置
     */
    public static void alterConfig() throws Exception {
        AdminClient adminClient = adminClient();

        // 组织两个参数
        // Map<ConfigResource, Config> configs = new HashMap<>();
        // ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, TOPIC_NAME);
        // Config config = new Config(Arrays.asList(new ConfigEntry("preallocate", "true")));
        // configs.put(configResource, config);
        // AlterConfigsResult alterConfigsResult = adminClient.alterConfigs(configs);

        // 2.3 版本以上的 API
        Map<ConfigResource, Collection<AlterConfigOp>> configs = new HashMap<>();
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, Constants.TOPIC_NAME);
        AlterConfigOp alterConfigOp = new AlterConfigOp(
                new ConfigEntry("preallocate", "false"), AlterConfigOp.OpType.SET);
        configs.put(configResource, Arrays.asList(alterConfigOp));
        AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(configs);

        alterConfigsResult.all().get();
    }

    /**
     * 查询配置
     */
    public static void describeConfig() throws Exception {
        AdminClient adminClient = adminClient();
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, Constants.TOPIC_NAME);
        // ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, TOPIC_NAME);
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        configResourceConfigMap.forEach((key, value) -> {
            System.out.println("key = " + key + ", value = " + value);
        });
    }

    /**
     * 描述 Topic
     */
    public static void describeTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(Constants.REPLICA_TOPIC_NAME));
        Map<String, TopicDescription> descriptionMap = describeTopicsResult.all().get();
        System.out.println("descriptionMap = " + descriptionMap);
    }

    /**
     * 删除 Topic
     */
    public static void deleteTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(Constants.TOPIC_NAME));
        deleteTopicsResult.all().get();
        System.out.println("deleteTopicsResult = " + deleteTopicsResult);
    }

    /**
     * 查看 Topic 列表
     */
    public static void listTopics() throws Exception {
        AdminClient adminClient = adminClient();
        ListTopicsResult listTopicsResult = adminClient.listTopics();
        Set<String> names = listTopicsResult.names().get();
        System.out.println("names = " + names);
    }

    /**
     * 创建 Topic
     */
    public static void createTopics() throws Exception {
        AdminClient adminClient = adminClient();
        // 日志副本数量
        short replicationFactor = 3;
        NewTopic newTopic = new NewTopic(Constants.REPLICA_TOPIC_NAME, 3, replicationFactor);
        CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(newTopic));
        createTopicsResult.all().get();
        System.out.println("createTopicsResult = " + createTopicsResult);
    }

    /**
     * 设置 AdminClient
     */
    public static AdminClient adminClient() {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.16.200:9092");
        AdminClient adminClient = AdminClient.create(properties);
        return adminClient;
    }

}
