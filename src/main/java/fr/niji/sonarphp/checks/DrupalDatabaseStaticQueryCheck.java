package fr.niji.sonarphp.checks;


import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = DrupalDatabaseStaticQueryCheck.KEY )
public class DrupalDatabaseStaticQueryCheck extends PHPVisitorCheck {

    public static final String KEY = "S5";
    public static final String D7_QUERY = "db_query";
    public static final String D8_QUERY = "query";

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        // D7
        if (callee.is(Kind.NAMESPACE_NAME) && ((NamespaceNameTree) callee).qualifiedName().equals(D7_QUERY)
                && tree.arguments().size() > 0) {

            ExpressionTree firstArgument = tree.arguments().get(0);
            this.checkDbQueryFirstArgument(firstArgument);
        }

        // D8
        if (callee.is(Kind.OBJECT_MEMBER_ACCESS) && ((MemberAccessTree) callee).member().toString().equals(D8_QUERY)) {

            ExpressionTree firstArgument = tree.arguments().get(0);
            this.checkDbQueryFirstArgument(firstArgument);
        }

        super.visitFunctionCall(tree);
    }

    private void checkDbQueryFirstArgument(ExpressionTree tree) {

        if(!tree.is(Kind.REGULAR_STRING_LITERAL)) {
            context().newIssue(this, tree, "Potential SQL Injection.");
        }
    }
}
