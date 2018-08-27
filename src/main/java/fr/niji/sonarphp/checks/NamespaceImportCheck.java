package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Set;


@Rule(key = NamespaceImportCheck.KEY )
public class NamespaceImportCheck extends PHPVisitorCheck {

    public static final String KEY = "S4";
    private static final Set<String> LOAD_FUNCTIONS = ImmutableSet.of("include","require", "include_once","require_once");
    private static final Set<String> EXCLUDED_FILES = ImmutableSet.of("autoload.php", "ScriptHandler.php");

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        if(!this.isExcludedFile()) {

            if (callee.is(Kind.NAMESPACE_NAME) && LOAD_FUNCTIONS.contains(((NamespaceNameTree) callee).qualifiedName())) {
                context().newIssue(this, callee, "Use namespace importing mechanism instead include/require functions.");
            }
        }

        super.visitFunctionCall(tree);
    }

    private boolean isExcludedFile() {
        String filename = context().getPhpFile().relativePath().getFileName().toString();
        return EXCLUDED_FILES.contains(filename);
    }
}
