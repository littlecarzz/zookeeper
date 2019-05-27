package zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 这里我们使用了同步计数器CountDownLatch，在connect方法中创建执行了zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
 * 之后，下边接着调用了CountDownLatch对象的await方法阻塞，因为这是zk客户端不一定已经完成了与服务端的连接，在客户端连接到服务端时会触发观察者调用process()方法，我们在方法里边判断一下触发事件的类型，完成连接后计数器减一，connect方法中解除阻塞。
 * 还有两个地方需要注意：这里创建的znode的访问权限是open的，且该znode是持久化存储的。
 * @author littlecar
 */
public class CreateGroup  implements Watcher {
    /**
     * 会话延时
     */
    private static final int SESSION_TIMEOUT=1000;
    private ZooKeeper zk=null;
    /**
     * 同步计数器
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    @Override
    public void process(WatchedEvent event) {
        /**
         * 客户端处于连接状态 - 它已连接,则-1
         */
        if(event.getState() == Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    /**
     *创建zk对象
     * 当客户端连上zookeeper时会执行process()里的countDownLatch.countDown()，计数器的值变为0，则countDownLatch.await()方法返回。
     * @param hosts
     * @throws InterruptedException
     * @throws IOException
     */
    public void connect(String hosts) throws InterruptedException, IOException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        countDownLatch.await();//阻塞程序继续执行
    }

    /**
     *创建group
     * @param groupName 组名
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void create(String groupName) throws KeeperException, InterruptedException {
        String path="/"+groupName;
//        Ids.OPEN_ACL_UNSAFE   允许任何客户端对该znode进行读写
//        CreateMode.PERSISTENT 持久化 PERSISTENT_SEQUENTIAL 持久化有序 EPHEMERAL 短暂性 EPHEMERAL_SEQUENTIAL 短暂性有序
        String createPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(createPath);
    }

    /**
     *关闭zk
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if(zk!=null){
            try {
                zk.close();
            } catch (InterruptedException e) {
                throw e;
            }finally {
                zk=null;
                System.gc();
            }
        }
    }
}
