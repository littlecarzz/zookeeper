package zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * 查找数据
 * @author littlecar
 */
public class WriteData extends ConnectionWatcher{
    private static final long RETRY_PERIOD_SECONDS=10000;
    private static final String CHARSET = "UTF-8";
    private static final int MAXRETRIES=5;
    public void write(String path, String value) throws UnsupportedEncodingException, InterruptedException, KeeperException {
        path="/"+path;
        int retries=0;
        while (true){
            try{
                Stat exists = zk.exists(path, false);
                if(exists==null){
                    zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }else{
                    zk.setData(path, value.getBytes(CHARSET), -1);
                }
            }catch (KeeperException.SessionExpiredException e){
                //此处会话过期，抛出异常，由上层调用来重新创建zookeeper对象
                throw e;
            }catch (KeeperException.AuthFailedException e) {
                //此处身份验证时，抛出异常，由上层来终止程序运行
                throw e;
            }catch(KeeperException e){
                //检查有没有超出尝试的次数
                if(retries == MAXRETRIES){
                    throw e;
                }
                retries++;
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }

    }
}
