package zookeeper;

import org.apache.zookeeper.KeeperException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class CreateGroupTest {
    private String hosts = "127.0.0.1";
    private String groupName = "zoo";
    private CreateGroup createGroup=null;

    /**
     * 初始化init
     * @throws IOException
     * @throws InterruptedException
     */
    @Before
    public void init() throws IOException, InterruptedException {
        createGroup=new CreateGroup();
        createGroup.connect(hosts);
    }
    @Test
    public void testCreateGroup() throws KeeperException, InterruptedException {
        createGroup.create(groupName);
    }
    @After
    public void destroy(){
        try {
            createGroup.close();
            createGroup=null;
            System.gc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
