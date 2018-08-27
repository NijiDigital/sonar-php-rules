package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Set;

@Rule(key = InputSuperglobalVariableUseCheck.KEY )
public class InputSuperglobalVariableUseCheck extends PHPVisitorCheck {

    private static final Set<String> UNSAFE_SUPERGLOBALS = ImmutableSet.of("$_GET", "$_POST", "$_COOKIE", "$_SERVER", "$_ENV");
    public static final String KEY = "S9";

    @Override
    public void visitVariableIdentifier(VariableIdentifierTree tree) {
        if(UNSAFE_SUPERGLOBALS.contains(tree.text())) {
            String issue = String.format("Do not use %s directly. Prefer using the filter_input() function.", tree.text());
            context().newIssue(this, tree, issue);
        }
        super.visitVariableIdentifier(tree);
    }
}
