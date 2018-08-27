package fr.niji.sonarphp.checks;


import com.google.common.collect.ImmutableMap;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ArrayInitializerFunctionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Rule(key = DrupalDatabaseDynamicQueryCheck.KEY )
public class DrupalDatabaseDynamicQueryCheck extends PHPVisitorCheck {

    public static final String KEY = "S6";
    public static final Map<String, List> DYNQUERY_METHODS = ImmutableMap.<String, List>builder()
            .put("condition", Arrays.asList(2))
            .put("where", Arrays.asList(0))
			.put("having", Arrays.asList(0))
            .put("havingCondition", Arrays.asList(2))
            .put("orderBy", Arrays.asList(0,1))
			.put("groupBy", Arrays.asList(0))
            .put("addExpression", Arrays.asList(0))
            .put("join", Arrays.asList(2))
            .put("innerJoin", Arrays.asList(2))
            .put("leftJoin", Arrays.asList(2))
            .put("rightJoin", Arrays.asList(2))
			.put("fields", Arrays.asList(0))
            .build();
    public static final String D8_DYNQUERY_METHODS = "query";

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        // D8
        if(callee.is(Kind.OBJECT_MEMBER_ACCESS)) {

            String methodName = ((MemberAccessTree) callee).member().toString();
            if(DYNQUERY_METHODS.containsKey(methodName)) {

                List<Integer> paramIndexes = DYNQUERY_METHODS.get(methodName);
                paramIndexes.forEach((paramIndex) -> {

                    if(tree.arguments().size() > paramIndex) {

                        this.checkArgument(tree.arguments().get(paramIndex));
                    }
                });
            }
        }

        super.visitFunctionCall(tree);
    }

    private void checkArgument(ExpressionTree tree) {

        if(!tree.is(Kind.REGULAR_STRING_LITERAL)) {
            context().newIssue(this, tree, "Potential SQL Injection.");
        }
    }
}
