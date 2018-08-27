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

import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Set;

@Rule(key = CURLTLSMisconfigurationCheck.KEY )
public class CURLTLSMisconfigurationCheck
        extends PHPVisitorCheck {

    public static final String KEY = "S3";
    private static final String CURL_SETOPT_FUNCTION = "curl_setopt";
    private static final Set<String> CURLOPT_SSL_CONF_KEYS = ImmutableSet.of("CURLOPT_SSLVERSION", "CURLOPT_SSL_CIPHER_LIST");

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        ExpressionTree callee = tree.callee();

        // Detect curl_setopt_function usage
        // http://php.net/manual/fr/function.curl-setopt.php
        if (callee.is(Kind.NAMESPACE_NAME) && ((NamespaceNameTree) callee).qualifiedName().equals(CURL_SETOPT_FUNCTION)) {

            // curl_setopt secound argument is the option key (CURLOPT_XXX)
            // Detect option settings that modify TLS protocol version
            ExpressionTree curlOptKey = tree.arguments().get(1);
            if(curlOptKey.is(Kind.NAMESPACE_NAME) &&
                    CURLOPT_SSL_CONF_KEYS.contains(((NamespaceNameTree) curlOptKey).qualifiedName())) {

                context().newIssue(this, callee, "Potential TLS misconfiguration.");
            }
        }

        // super method must be called in order to visit function call node's children
        super.visitFunctionCall(tree);
    }
}
