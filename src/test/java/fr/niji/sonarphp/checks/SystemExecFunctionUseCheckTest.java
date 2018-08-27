package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PhpTestFile;
import org.sonar.plugins.php.api.tests.PHPCheckTest;

import java.io.File;

public class SystemExecFunctionUseCheckTest {

  @Test
  public void test() throws Exception {
    PHPCheckTest.check(new SystemExecFunctionUseCheck(), new PhpTestFile(new File("src/test/resources/checks/systemExecFunctionUseCheck.php")));
  }

}
