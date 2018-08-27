package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.tests.PhpTestFile;

import java.io.File;

public class DrupalFormStateUserInputUseCheckTest {

    @Test
    public void testD8() throws Exception {
        File file =  new File("src/test/resources/checks/d8FormStateGetUserInputUseCheck.php");
        PHPCheckTest.check(new DrupalFormStateUserInputUsageCheck(), new PhpTestFile(file));
    }


    @Test
    public void testD7() throws Exception {
        File file =  new File("src/test/resources/checks/d7FormStateGetUserInputUseCheck.php");
        PHPCheckTest.check(new DrupalFormStateUserInputUsageCheck(), new PhpTestFile(file));
    }
}
