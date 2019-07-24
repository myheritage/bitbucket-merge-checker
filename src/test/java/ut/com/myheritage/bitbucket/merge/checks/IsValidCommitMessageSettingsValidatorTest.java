package com.myheritage.bitbucket.merge.checks;

import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.atlassian.bitbucket.hook.repository.*;
import com.atlassian.bitbucket.setting.SettingsValidationErrors;
import com.atlassian.bitbucket.setting.SettingsValidator;
import com.atlassian.bitbucket.scope.Scope;
import com.myheritage.bitbucket.merge.checks.IsValidCommitMessageSettingsValidator;
import com.atlassian.bitbucket.setting.Settings;


@RunWith (MockitoJUnitRunner.class)
public class IsValidCommitMessageSettingsValidatorTest
{
    @Mock
    private Settings settings;

    @Mock
    private SettingsValidationErrors errors;

    @Mock
    private Scope scope;

    

    private IsValidCommitMessageSettingsValidator validator;


    @Before 
    public void initialize() {
        validator = new IsValidCommitMessageSettingsValidator();
        setMockSettingsGetStringMethod("mergeCommitMsgRegex", "Feat:(.*)");
        setMockSettingsGetStringMethod("mergeCommitMsgSummaryText", "Mock summary notification message is required");
        setMockSettingsGetStringMethod("mergeCommitMsgDetailedText", "Mock summary notification message is required");
    }

    @Test
    public void testValidateEmptyRegex()
    {   
        setMockSettingsGetStringMethod("mergeCommitMsgRegex", "");
        
        validator.validate(settings, errors, scope);

        verify(errors).addFieldError("mergeCommitMsgRegex", "A Regex is required");
    }

    @Test
    public void testValidateEmptySummaryText()
    {   
        setMockSettingsGetStringMethod("mergeCommitMsgSummaryText", "");
        
        validator.validate(settings, errors, scope);

        verify(errors).addFieldError("mergeCommitMsgSummaryText", "A summary notification message is required");
    }

    @Test
    public void testValidateEmptyDetailsText()
    {   
        setMockSettingsGetStringMethod("mergeCommitMsgDetailedText", "");
        
        validator.validate(settings, errors, scope);

        verify(errors).addFieldError("mergeCommitMsgDetailedText", "A detailed notification message is required");
    }

    private void setMockSettingsGetStringMethod(String prop, String value) {
        when(settings.getString(prop)).thenReturn(value);
    }
}