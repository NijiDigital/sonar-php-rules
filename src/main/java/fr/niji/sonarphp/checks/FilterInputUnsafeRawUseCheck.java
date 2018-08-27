package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;

import java.util.function.Function;
import java.util.Optional;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = FilterInputUnsafeRawUseCheck.KEY )
public class FilterInputUnsafeRawUseCheck extends PHPVisitorCheck {

    private static final String FILTER_INPUT_FUNCTION = "filter_input";
    private static final Set<String> UNSAFE_FILTER_IDS = ImmutableSet.of("FILTER_DEFAULT", "FILTER_UNSAFE_RAW");
    public static final String KEY = "S10";

  @Override
  public void visitFunctionCall(FunctionCallTree tree) {
      ExpressionTree callee = tree.callee();

      if (callee.is(Kind.NAMESPACE_NAME) && ((NamespaceNameTree) callee).qualifiedName().equals(FILTER_INPUT_FUNCTION)) {


          // Not specifying the filter parameter is equivalent to have FILTER_DEFAULT value (unsafe raw filter)
          // http://php.net/manual/fr/function.filter-input.php
          if(tree.arguments().size() < 3) {
              this.unsafeFilterIssue(tree,"FILTER_DEFAULT");
          } else {

              // Tree type checking function
              Function<ExpressionTree, Optional<NamespaceNameTree>> isNamespaceNameTree = (treeValue) -> {
                  if(treeValue.is(Kind.NAMESPACE_NAME)) {
                      return Optional.of((NamespaceNameTree) treeValue);
                  } else {
                      return Optional.empty();
                  }
              };

              // filter_input third argument sets the filter option
              ExpressionTree filterIdValue = tree.arguments().get(2);
              // Tree type checking
              isNamespaceNameTree.apply(filterIdValue)
                      .map( (namespaceNameTree)-> namespaceNameTree.qualifiedName())
                      .flatMap( (String filterName) -> {

                          // Check filterId value against unsafe filer ids list
                          if(UNSAFE_FILTER_IDS.contains(filterName)) {
                              return Optional.of(filterName);
                          } else {
                              return Optional.empty();
                          }
                      })
                      .ifPresent( (String filterName) -> this.unsafeFilterIssue(tree, filterName));
          }

      }

      super.visitFunctionCall(tree);
  }

  private void unsafeFilterIssue(ExpressionTree tree, String filterId) {
      String issue = String.format("filter_input() should not be used with %s filter option.", filterId);
      context().newIssue(this, tree, issue);
  }
}
