package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

public class DrupalDatabaseStaticQueryCheckTest {

    @Test
    public void test() throws Exception {
        File file =  new File("src/test/resources/checks/drupalDatabaseStaticQueryCheck.php");
        PHPCheckTest.check(new DrupalDatabaseStaticQueryCheck(), new PhpTestFile(file));
    }
}
