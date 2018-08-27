package fr.niji.sonarphp.checks;

import com.google.common.collect.ImmutableSet;
import org.sonar.check.Rule;
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

@Rule(key = CURLDisableTLSCheck.KEY )
public class CURLDisableTLSCheck extends PHPVisitorCheck {

    public static final String KEY = "S2";
    private static final String CURL_SETOPT_FUNCTION = "curl_setopt";
    private static final Set<String> CURLOPT_SSL = ImmutableSet.of("CURLOPT_SSL_VERIFYHOST","CURLOPT_SSL_VERIFYPEER");

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        // Detect curl_setopt_function usage
        // http://php.net/manual/fr/function.curl-setopt.php
        if (callee.is(Kind.NAMESPACE_NAME) && ((NamespaceNameTree) callee).qualifiedName().equals(CURL_SETOPT_FUNCTION)) {

            // curl_setopt secound argument is the option key (CURLOPT_XXX)
            // Detect option settings that disable TLS trust chain verification
            ExpressionTree curlOptKey = tree.arguments().get(1);
            if(curlOptKey.is(Kind.NAMESPACE_NAME) && CURLOPT_SSL.contains(((NamespaceNameTree) curlOptKey).qualifiedName())) {

                // curl_setopt third argument for TLS options is a Boolean value
                ExpressionTree curlOptValue = tree.arguments().get(2);

                // Voir http://php.net/manual/fr/language.types.boolean.php
                Boolean optDisabled = false;
                Boolean optValueUnknown = false;
                if(curlOptValue.is(Kind.BOOLEAN_LITERAL)) {
                    // Detect false or FALSE boolean values
                    Boolean value = Boolean.parseBoolean(((LiteralTree )curlOptValue).value());
                    optDisabled = !value;
                } else if (curlOptValue.is(Kind.NULL_LITERAL)) {
                    // Detect NULL literal values
                    optDisabled = true;
                } else if (curlOptValue.is(Kind.NUMERIC_LITERAL)) {
                    // Detect integer or floating literals equals to zero
                    String value = ((LiteralTree )curlOptValue).value();
                    try {
                        optDisabled = NumberFormat.getInstance().parse(value).intValue() == 0;
                    } catch (ParseException exception) {
                        optValueUnknown = true;
                    }
                } else if (curlOptValue.is(Kind.REGULAR_STRING_LITERAL)) {
                    // Detect empty string ("") on zero character string ("0")
                    ImmutableSet emptyOrZeroCharStringLiteral = ImmutableSet.of("\"\"", "\"0\"");
                    optDisabled = emptyOrZeroCharStringLiteral.contains(((LiteralTree )curlOptValue).value());
                } else if (curlOptValue.is(Kind.ARRAY_INITIALIZER_FUNCTION)) {
                    // Detect empty array literal
                    optDisabled = ((ArrayInitializerFunctionTree) curlOptValue).arrayPairs().size() == 0;
                } else {
                    optValueUnknown = true;
                }

                if(optDisabled) {
                    context().newIssue(this, callee, "TLS trust chain verification should not be disabled.");
                } else if (optValueUnknown) {
                    context().newIssue(this, callee, "Potential TLS trust chain misconfiguration.");
                }
            }
        }

        // super method must be called in order to visit function call node's children
        super.visitFunctionCall(tree);
    }
}
