/*
 * SonarQube PHP Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package fr.niji.sonarphp;


import com.google.common.collect.ImmutableList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import fr.niji.sonarphp.checks.*;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.php.api.visitors.PHPCustomRuleRepository;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * Extension point to define a PHP rule repository.
 */
public class PHPRuleDefinitions implements RulesDefinition, PHPCustomRuleRepository {

  private static final String RESOURCE_BASE_PATH = "fr/niji/l10n/php/rules/custom";

  @Override
  public String repositoryKey() {

    return "niji-php-rules";
  }

  /**
   * Provide the list of checks class that implements rules
   * to be part of the rule repository
   */
  @Override
  public ImmutableList<Class> checkClasses() {
      return ImmutableList.of(
              CURLDisableTLSCheck.class,
              CURLTLSMisconfigurationCheck.class,
              NamespaceImportCheck.class,
              DrupalDatabaseStaticQueryCheck.class,
              DrupalDatabaseDynamicQueryCheck.class,
              InputSuperglobalVariableUseCheck.class,
              DrupalFormStateUserInputUsageCheck.class,
              FilterInputUnsafeRawUseCheck.class
      );
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(repositoryKey(), "php").setName("Niji PHP Rules");

    RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH);
    ruleMetadataLoader.addRulesByAnnotatedClass(repository, this.checkClasses());

    repository.done();
  }

  private String loadResource(String path) {
    URL resource = getClass().getResource(path);
    if (resource == null) {
      throw new IllegalStateException("Resource not found: " + path);
    }
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    try (InputStream in = resource.openStream()) {
      byte[] buffer = new byte[1024];
      for (int len = in.read(buffer); len != -1; len = in.read(buffer)) {
        result.write(buffer, 0, len);
      }
      return new String(result.toByteArray(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read resource: " + path, e);
    }
  }
}
