package com.enmotech.kafkatest.util;

import com.enmotech.kafkatest.pojo.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * com.enmotech.kafkatest.util
 *
 * @author syf
 * @create 2022-08-23-14:22
 * @Description kafka-test
 */
@Component
public class SchemaUtil {

    //生成随机数据所用的数组和类
    char[] PayloadHouse = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm','o','p','q','r','s','t','我', '你', '他', '是', '否', '对', '错', '好', '坏', '快', '慢'};
    char[] TopicHouse = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm','o','p','q','r','s','t','0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    Random random = new Random();

    //操作kafka中topic的对象
    //需要在主启动类上配置注解@EnableConfigurationProperties(KafkaProperties.class)
    @Autowired
    private KafkaProperties properties;

    public Schema getSchema(){
        Field[] fields = Field.getFields();
        Schema schema = new Schema("struct",fields,false,"demo_table");
        return schema;
    }

    @Bean
    public JsonDemo getJsonDemo(){
        Schema schema = getSchema();
        Payload payload = new Payload();
        JsonDemo jsondemo = new JsonDemo(schema,payload);
        return jsondemo;
    }

    //给payload中的属性赋值
    public void setPayload(int count, Payload payload, int target){
        //得到日期类对象
        Calendar calendar = Calendar.getInstance();
        //设置日期时间格式转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //生成随机数和随机字符串
        //经纬度
        Double longitude = random.nextDouble()*180;
        Double latitude = random.nextDouble()*180;

        //温度湿度，保留两位小数
        Double temperature = Double.valueOf(String.format("%.2f",(random.nextDouble()*50-10)));
        Double humidity = Double.valueOf(String.format("%.2f",random.nextDouble()*100));
        //对当前日期时间 随机加减月数和天数
        calendar.add(Calendar.MONTH,(random.nextInt(24)-12));
        calendar.add(Calendar.DATE,(random.nextInt(60)-30));
        calendar.add(Calendar.HOUR,(random.nextInt(24)-12));
        calendar.add(Calendar.MINUTE,(random.nextInt(120)-60));
        calendar.add(Calendar.SECOND,(random.nextInt(120)-60));
        long Timestamp = calendar.getTimeInMillis();
        String stringtime = sdf.format(Timestamp);
        //生成随机字符串
        String randomString = RandomStringUtils.random(20,PayloadHouse);
        int temp = random.nextInt(10);
        //随机在数据中插入null
        switch (temp){
            case 2:
                longitude = null;
                break;
            case 3:
                latitude = null;
                break;
            case 4:
                temperature = null;
                break;
            case 5:
                humidity = null;
                break;
        }
        if (temp<target){
            randomString = null;
        }
        payload.setId(count);
        payload.setHumidity(humidity);
        payload.setLatitude(latitude);
        payload.setLongitude(longitude);
        payload.setStringtime(stringtime);
        payload.setTemperature(temperature);
        payload.setRandomstring(randomString);
        payload.setTabletime(Timestamp);
    }

    //随机生成一定数量的topic,如果没有指定quantity则随机生成，否则生成指定数量的topic
    public String[] getRandomTopics(String prefix,Integer quantity){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd'_'HH-mm-ss");
        String nowTime = sdf.format(System.currentTimeMillis());
        if (prefix == null){
            prefix = "topic_";
        }else if (!prefix.endsWith("_")){
            prefix = prefix+"_";
        }
        String[] topics;
        if (quantity != null){
            topics = new String[quantity];
            for (int i = 0; i < quantity; i++) {
                topics[i] = prefix+nowTime+"_"+i;
            }
            return topics;
        }
        int temp = (random.nextInt(100)+1);
        topics = new String[temp];
        for (int i = 0; i < temp; i++) {
            topics[i] = prefix+nowTime+"_"+i;
        }
        creatTopic(topics);
        return topics;
    }

    //创建、初始化真实的topics
    public void creatTopic(String[] topics) {
        AdminClient client = AdminClient.create(properties.buildAdminProperties());
        //当client正确创建了，开始创建新的topic
        if (client != null) {
            try {
                //获取kafka服务器中的topics集合
                ListTopicsResult listTopicsResult = client.listTopics();
                //获取topics名称
                Set<String> names = listTopicsResult.names().get();
                //当kafka服务器中不存在指定的topic时，创建新的topic
                Collection newTopics = new ArrayList<NewTopic>();
                for (int i = 0; i < topics.length; i++) {
                    if (!names.contains(topics[i])) {
                        newTopics.add(new NewTopic(topics[i], 1, (short) 1));
                    }
                }
                client.createTopics(newTopics);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                client.close();
            }
        }
    }
}
