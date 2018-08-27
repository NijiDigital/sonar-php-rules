package fr.niji.sonarphp;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.junit.Assert.assertEquals;

public class PHPRuleDefinitionsTest {

  @Test
  public void rules() {
    PHPRuleDefinitions rulesDefinition = new PHPRuleDefinitions();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("niji-php-rules");
    assertEquals(10, repository.rules().size());
  }
}
