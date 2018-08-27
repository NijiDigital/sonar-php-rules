package fr.niji.sonarphp.checks;

import org.junit.Test;
import org.sonar.plugins.php.api.tests.PhpTestFile;
import org.sonar.plugins.php.api.tests.PHPCheckTest;

import java.io.File;

public class FilterInputUnsafeRawUseCheckTest {

  @Test
  public void test() throws Exception {
    PHPCheckTest.check(new FilterInputUnsafeRawUseCheck(), new PhpTestFile(new File("src/test/resources/checks/filterInputUnsafeRawUseCheck.php")));
  }

}
