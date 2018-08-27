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

@Rule(key = SystemExecFunctionUseCheck.KEY )
public class SystemExecFunctionUseCheck extends PHPVisitorCheck {

  private static final Set<String> FORBIDDEN_FUNCTIONS = ImmutableSet.of("exec", "passthru", "proc_open", "popen", "shell_exec", "system", "pcntl_exec");
  public static final String KEY = "S1";

  @Override
  public void visitFunctionCall(FunctionCallTree tree) {
      ExpressionTree callee = tree.callee();

      if (callee.is(Kind.NAMESPACE_NAME) && FORBIDDEN_FUNCTIONS.contains(((NamespaceNameTree) callee).qualifiedName())) {
          context().newIssue(this, callee, "Potential remote code execution.");
      }

      super.visitFunctionCall(tree);
  }

  public void visitLiteral(LiteralTree tree) {
      if (tree.value().startsWith("`")) {
          context().newIssue(this, tree, "Potential remote code execution.");
      }
      super.visitLiteral(tree);
  }
}
