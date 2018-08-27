package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

public class CURLDisableTLSCheckTest {

    @Test
    public void test() throws Exception {
        PHPCheckTest.check(new CURLDisableTLSCheck(), new PhpTestFile(new File("src/test/resources/checks/cURLDisableTLSCheck.php")));
    }
}
