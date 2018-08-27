package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

public class NamespaceImportCheckTest {

    @Test
    public void test() throws Exception {
        PHPCheckTest.check(new NamespaceImportCheck(), new PhpTestFile(new File("src/test/resources/checks/namespaceImportCheck.php")));
    }

    @Test
    public void test_exclude() throws Exception {
        PHPCheckTest.check(new NamespaceImportCheck(), new PhpTestFile(new File("src/test/resources/checks/autoload.php")));
    }
}
