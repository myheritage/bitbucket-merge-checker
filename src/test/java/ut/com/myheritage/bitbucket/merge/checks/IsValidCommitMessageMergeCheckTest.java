package com.myheritage.bitbucket.merge.checks;

import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

import com.atlassian.bitbucket.hook.repository.*;
import com.atlassian.bitbucket.i18n.I18nService;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionService;
import com.myheritage.bitbucket.merge.checks.IsValidCommitMessageMergeCheck;
import com.atlassian.bitbucket.setting.Settings;


@RunWith (MockitoJUnitRunner.class)
public class IsValidCommitMessageMergeCheckTest
{
    @Mock
    private PreRepositoryHookContext preRepositoryHookContext;

    @Mock
    private I18nService i18nService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private PreRepositoryHookContext context;

    @Mock
    private PullRequestMergeHookRequest request;

    @Mock
    private Settings contextSettings;

    private IsValidCommitMessageMergeCheck checker;


    @Before 
    public void initialize() {
        checker = new IsValidCommitMessageMergeCheck(i18nService, permissionService);
        when(contextSettings.getString("mergeCommitMsgSummaryText")).thenReturn("reject Summary message");
        when(contextSettings.getString("mergeCommitMsgDetailedText")).thenReturn("reject Detailed message");
        when(i18nService.getText(any(), any())).thenReturn("reject message");
    }

    @Test
    public void testRejectedPreUpdate()
    {
        setMockTestedCommitMsg("This is a merge commit message");
        setMockCommitMsgRegex("Feat:(.*)");

        RepositoryHookResult repositoryHookResult = checker.preUpdate(context, request);

        assertEquals("Merge should be rejected", repositoryHookResult.isRejected(), true);
    }

    @Test
    public void testAcceptedPreUpdate()
    {
        setMockTestedCommitMsg("Feat: This is a merge commit message");
        setMockCommitMsgRegex("Feat:(.*)");

        RepositoryHookResult repositoryHookResult = checker.preUpdate(context, request);

        assertEquals("Merge should be accepted", repositoryHookResult.isAccepted(), true);
    }

    private void setMockTestedCommitMsg(String msg) {
        String commitMsg = new String(msg);
        Optional<String> opt = Optional.of(commitMsg);
        when(request.getMessage()).thenReturn(opt);
    }

    private void setMockCommitMsgRegex(String regex) {
        when(contextSettings.getString("mergeCommitMsgRegex", ".")).thenReturn(regex);
        when(context.getSettings()).thenReturn(contextSettings);
    }
}