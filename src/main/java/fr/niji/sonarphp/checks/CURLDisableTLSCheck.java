package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.php.checks.utils.CheckUtils;
import org.sonar.php.tree.visitors.AssignmentExpressionVisitor;
import org.sonar.plugins.php.api.symbols.Symbol;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.expression.ArrayInitializerFunctionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Set;

@Rule(key = CURLDisableTLSCheck.KEY)
public class CURLDisableTLSCheck extends PHPVisitorCheck {

    public static final String KEY = "S2";
    private static final String CURL_SETOPT = "curl_setopt";
    private static final String CURLOPT_SSL_VERIFYHOST = "CURLOPT_SSL_VERIFYHOST";
    private static final String CURLOPT_SSL_VERIFYPEER = "CURLOPT_SSL_VERIFYPEER";

    private static final String MESSAGE = "Change this code to enable trust chain verification.";
    private AssignmentExpressionVisitor assignmentExpressionVisitor;

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        assignmentExpressionVisitor = new AssignmentExpressionVisitor(context().symbolTable());
        tree.accept(assignmentExpressionVisitor);
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        String functionName = CheckUtils.getFunctionName(tree);
        List<ExpressionTree> arguments = tree.arguments();

        // Detect curl_setopt function usage
        // http://php.net/manual/fr/function.curl-setopt.php
        if (CURL_SETOPT.equals(functionName) && arguments.size() > 2) {
            ExpressionTree optionArgument = arguments.get(1);
            if (optionArgument.is(Tree.Kind.NAMESPACE_NAME) &&
                    CURLOPT_SSL_VERIFYHOST.equals(((NamespaceNameTree) optionArgument).name().text())) {
                checkCURLSSLVerifyHost(getAssignedValue(arguments.get(2)));
            } else if (optionArgument.is(Tree.Kind.NAMESPACE_NAME) &&
                    CURLOPT_SSL_VERIFYPEER.equals(((NamespaceNameTree) optionArgument).name().text())) {
                checkCURLSSLVerifyPeer(getAssignedValue(arguments.get(2)));
            }
        }

        // super method must be called in order to visit function call node's children
        super.visitFunctionCall(tree);
    }

    private void checkCURLSSLVerifyHost(ExpressionTree curlOptValue) {

        Boolean verifyHostEnabled = false;
        if (curlOptValue.is(Kind.NUMERIC_LITERAL)) {
            // Detect 2 integer value
            String value = ((LiteralTree) curlOptValue).value();
            verifyHostEnabled = (value == "2");
        } else if (curlOptValue.is(Kind.REGULAR_STRING_LITERAL)) {
            // Detect '2' or "2" string characters
            ImmutableSet twoStringLiteral = ImmutableSet.of("\'2\'", "\"2\"");
            verifyHostEnabled = twoStringLiteral.contains(((LiteralTree) curlOptValue).value());
        }

        if (!verifyHostEnabled) {
            context().newIssue(this, curlOptValue, MESSAGE);
        }
    }

    private void checkCURLSSLVerifyPeer(ExpressionTree curlOptValue) {

        Boolean verifyPeerEnabled = false;
        if (curlOptValue.is(Kind.BOOLEAN_LITERAL)) {
            // Detect true or TRUE boolean values
            ImmutableSet trueStringLiteral = ImmutableSet.of("true", "TRUE");
            verifyPeerEnabled = Boolean.parseBoolean(((LiteralTree) curlOptValue).value());

        } else if (curlOptValue.is(Kind.NUMERIC_LITERAL)) {
            // Detect 1 integer value
            String value = ((LiteralTree) curlOptValue).value();
            verifyPeerEnabled = (value == "1");
        } else if (curlOptValue.is(Kind.REGULAR_STRING_LITERAL)) {
            // Detect '1' or "1" string characters
            ImmutableSet twoStringLiteral = ImmutableSet.of("\'1\'", "\"1\"");
            verifyPeerEnabled = twoStringLiteral.contains(((LiteralTree) curlOptValue).value());
        }

        if (!verifyPeerEnabled) {
            context().newIssue(this, curlOptValue, MESSAGE);
        }
    }

    private ExpressionTree getAssignedValue(ExpressionTree value) {
        if (value.is(Tree.Kind.VARIABLE_IDENTIFIER)) {
            Symbol valueSymbol = context().symbolTable().getSymbol(value);
            return assignmentExpressionVisitor
                    .getUniqueAssignedValue(valueSymbol)
                    .orElse(value);
        }
        return value;
    }
}
