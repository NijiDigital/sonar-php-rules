package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = UnserializeFunctionUseCheck.KEY )
public class UnserializeFunctionUseCheck extends PHPVisitorCheck {

    private static final String UNSERIALIZE_FUNCTION = "unserialize";
  public static final String KEY = "S7";

  @Override
  public void visitFunctionCall(FunctionCallTree tree) {
      ExpressionTree callee = tree.callee();

      if (callee.is(Kind.NAMESPACE_NAME) && ((NamespaceNameTree) callee).qualifiedName().equals(UNSERIALIZE_FUNCTION)) {
          context().newIssue(this, callee, "Potential object injection.");
      }

      super.visitFunctionCall(tree);
  }
}
