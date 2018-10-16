package fr.niji.sonarphp;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.php.checks.CheckList;
import org.sonar.plugins.php.api.Php;

import static org.assertj.core.api.Assertions.assertThat;

public class DrupalSecurityProfileDefinitionTest  {

    @Test
    public void profile_creation() {
        ValidationMessages validation = ValidationMessages.create();

        DrupalSecurityProfileDefinition definition = new DrupalSecurityProfileDefinition(new FakeProfileParser());
        RulesProfile profile = definition.createProfile(validation);

        assertThat(profile.getLanguage()).isEqualTo(Php.KEY);
        assertThat(profile.getName()).isEqualTo("Drupal Security");
        assertThat(profile.getActiveRulesByRepository(CheckList.REPOSITORY_KEY)).hasSize(23);
        assertThat(profile.getActiveRulesByRepository(PHPRuleDefinitions.REPOSITORY_KEY)).hasSize(7);
        assertThat(validation.hasErrors()).isFalse();
    }

}
