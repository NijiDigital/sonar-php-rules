package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

public class DrupalDatabaseDynamicQueryCheckTest {

    @Test
    public void test() throws Exception {
        File file =  new File("src/test/resources/checks/drupalDatabaseDynamicQueryCheck.php");
        PHPCheckTest.check(new DrupalDatabaseDynamicQueryCheck(), new PhpTestFile(file));
    }
}
