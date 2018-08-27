package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = DrupalFormStateUserInputUsageCheck.KEY )
public class DrupalFormStateUserInputUsageCheck extends PHPVisitorCheck {

    private static final String D7_FORM_STATE_ARRAY_COMMON_NAME = "$form_state";
    private static final String D7_FORM_STATE_ARRAY_INPUT_KEY = "input";
    private static final String D8_GET_USER_INPUT_METHOD = "getUserInput";
    public static final String KEY = "S8";

    // D8
    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        if (callee.is(Kind.OBJECT_MEMBER_ACCESS) && ((MemberAccessTree) callee).member().toString().equals(D8_GET_USER_INPUT_METHOD)) {

            context().newIssue(this, tree, "Do not use the raw $form_state->getUserInput(). Use $form_state->getValue() instead.");
        }

        super.visitFunctionCall(tree);
    }

    // D7
    @Override
    public void visitArrayAccess(ArrayAccessTree tree) {

        if (tree.object().toString().equals(D7_FORM_STATE_ARRAY_COMMON_NAME)
                && tree.offset().toString().toLowerCase().replaceAll("['\"]", "").equals(D7_FORM_STATE_ARRAY_INPUT_KEY)) {

            context().newIssue(this, tree, "Do not use the raw $form_state['input'], use $form_state['values'] instead.");
        }
        super.visitArrayAccess(tree);
    }

}
