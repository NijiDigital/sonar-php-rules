package fr.niji.sonarphp;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FakeProfileParser extends XMLProfileParser {

    public FakeProfileParser() {
        super(ruleFinder());
    }

    private static RuleFinder ruleFinder() {
        return when(mock(RuleFinder.class).findByKey(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
            public Rule answer(InvocationOnMock invocation) {
                Object[] arguments = invocation.getArguments();
                return Rule.create((String) arguments[0], (String) arguments[1], (String) arguments[1]);
            }
        }).getMock();
    }

}
