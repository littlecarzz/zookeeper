package zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class FindGroupTest extends ConnectionWatcher {
    public Stat find(String groupName){
        String path="/"+groupName;
        try {
            Stat exists = zk.exists(path, false);
            return exists;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        FindGroupTest findGroupTest=new FindGroupTest();
        findGroupTest.connection("127.0.0.1");
        Stat exists = findGroupTest.find("zoo");
        System.out.println(exists==null);
        findGroupTest.close();
    }
}
