package fr.niji.sonarphp;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

public class DrupalSecurityProfileDefinition extends ProfileDefinition {

    private final XMLProfileParser xmlProfileParser;

    public DrupalSecurityProfileDefinition(XMLProfileParser xmlProfileParser) {
        this.xmlProfileParser = xmlProfileParser;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validation) {
        return xmlProfileParser.parseResource(getClass().getClassLoader(), "fr/niji/sonarphp/profile/drupal-security-profile.xml", validation);
    }
}
