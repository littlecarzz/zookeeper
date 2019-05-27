package zookeeper;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class WriteDataTest extends ConnectionWatcher {
    private String hosts = "127.0.0.1";
    private int SESSION_TIMEOUT = 5000;
    private WriteData writeData=null;
    @Before
    public void init() throws IOException, InterruptedException {
        writeData = new WriteData();
        writeData.connection(hosts);
    }
    @Test
    public void test() throws InterruptedException, IOException {
        int flag = 0;

        while (true) {
            try {
                writeData.write("zoo", "chejunjie666");
                break;
            } catch (KeeperException.SessionExpiredException e) {
                // TODO: 重新创建、开始一个新的会话
                e.printStackTrace();
                zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
            }catch(KeeperException.AuthFailedException e){
                //TODO 此处身份验证时，终止程序运行
                e.printStackTrace();
                flag = 1;
                break;
            } catch (KeeperException e) {
                // TODO 尝试了多次，还是出错，只有退出了
                e.printStackTrace();
                flag = 1;
                break;
            }catch (IOException e) {
                // TODO 创建zookeeper对象失败，无法连接到zk集群
                e.printStackTrace();
                flag = 1;
                break;
            }
        }
        System.exit(flag);
    }
    @After
    public void destroy(){
        if(zk !=null){
            try {
                writeData.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                writeData=null;
                System.gc();
            }
        }
    }
}
