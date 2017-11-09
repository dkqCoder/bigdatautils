package com.kq.hbase.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created with Intellij IDEA
 * Create User: keqiang.du
 * Date: 2017/9/11
 * Time: 下午2:44
 * Description:
 */
public class ConfigurationFactory {

    static Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);

    public static Configuration getConfiguration(String path)  {
        Configuration conf = new Configuration();
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (Exception e) {
            logger.error("System.getProperty(\"JJE_CONFIG_HOME\"):"+ System.getProperty("JJE_CONFIG_HOME"));
            logger.error("file not found path:"+path,e);
            return null;        }
        conf.addResource("hbase-default.xml");
        conf.addResource(stream);
        return conf;
    }

    public static Configuration getConfigurationByQuorum(String quorum)  {
        Configuration conf  = HBaseConfiguration.create();
        conf = HBaseConfiguration.create(conf);
        conf.set("hbase.zookeeper.quorum",quorum);
        return conf;
    }
}
