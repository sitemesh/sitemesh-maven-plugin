import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.sitemesh.maven.SiteMeshMojo;

import java.io.File;

/**
 * @author Richard L. Burton III - SmartCode LLC
 */
public class SiteMeshMojoTestCase extends AbstractMojoTestCase {


    /**
     * @throws Exception
     */
    public void testMojoGoal() throws Exception {
        File testPom = new File(getBasedir(), "src/test/resources/test1/sitemesh-test-plugin-config.xml");
        SiteMeshMojo mojo = (SiteMeshMojo) lookupMojo("sitemesh", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }

}
