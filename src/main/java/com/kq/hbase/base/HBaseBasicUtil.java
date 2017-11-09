package com.kq.hbase.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created with Intellij IDEA
 * Create User: keqiang.du
 * Date: 2017/9/11
 * Time: 下午2:17
 * Description:
 */
public class HBaseBasicUtil {
//    @Value("${hbase.conf.path}")
    protected String confPath = "";
    protected String hbaseNameSpace = "";

    private static final Logger logger = LoggerFactory.getLogger(HBaseBasicUtil.class);
    static Configuration conf;
    static Connection conn;

    protected Connection getConnection() throws IOException {
        if(conn==null){
            logger.error("=========getConnection start=========");
            conn = ConnectionFactory.createConnection(getConfiguration());
            logger.error("=========getConnection start=========");
        }
        return conn;
    }

    protected Configuration getConfiguration() {
        if(conf==null){
            logger.error("=========getConfiguration start=========");
            conf = ConfigurationFactory.getConfiguration(confPath);
            conf.set("hbase.client.scanner.timeout.period", "3000000");
            logger.error("=========getConfiguration end=========");
        }
        return conf;
    }

    protected void closeTable(Table table) {
        try {
            if(table!=null){
                table.close();
            }
        } catch (IOException e) {
            logger.error("close table error",e);
        }
    }
}
