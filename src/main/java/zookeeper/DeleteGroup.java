package zookeeper;

import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * 删除分组
 * @author littlecar
 *
 */
public class DeleteGroup extends ConnectionWatcher {
    public void delete(String groupName) {
        String path = "/" + groupName;
        
        try {
            List<String> children = zk.getChildren(path, false);
            
            for(String child : children){
                zk.delete(path + "/" + child, -1);
            }
            /**
             * zk.delete(path,version)方法的第二个参数是znode版本号，如果提供的版本号和znode版本号一致才会删除这个znode，这样可以检测出对znode的修改冲突。
             * 通过将版本号设置为-1，可以绕过这个版本检测机制，无论znode的版本号是什么，都会直接将其删除。
             */
            zk.delete(path, -1);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}