package com.myheritage.bitbucket.merge.checks;

import com.atlassian.bitbucket.hook.repository.*;
import com.atlassian.bitbucket.i18n.I18nService;
import com.atlassian.bitbucket.permission.Permission;
import com.atlassian.bitbucket.permission.PermissionService;
import com.atlassian.bitbucket.repository.Repository;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.regex.*;  

import javax.annotation.Nonnull;

@Component("isValidCommitMessageMergeCheck")
public class IsValidCommitMessageMergeCheck implements RepositoryMergeCheck {
    private final I18nService i18nService;
    private final PermissionService permissionService;

    @Autowired
    public IsValidCommitMessageMergeCheck(@ComponentImport I18nService i18nService, 
                             @ComponentImport PermissionService permissionService) {
        this.i18nService = i18nService;
        this.permissionService = permissionService;
    }

    @Nonnull
    @Override
    public RepositoryHookResult preUpdate(@Nonnull PreRepositoryHookContext context,
                                          @Nonnull PullRequestMergeHookRequest request) {
        if (!request.isDryRun()) {
            String msg = request.getMessage().orElse(null);
            // Retrieve the relevant parameters from the hook settings
            String mergeCommitMsgRegex = context.getSettings().getString("mergeCommitMsgRegex", ".");
            String mergeCommitMsgSummaryText = context.getSettings().getString("mergeCommitMsgSummaryText");
            String mergeCommitMsgDetailedText = context.getSettings().getString("mergeCommitMsgDetailedText");
            // String pattern = mergeCommitMsgRegex;
            boolean isValidMessage = msg.matches(mergeCommitMsgRegex);
            if (!isValidMessage) {
                String summaryMsg = i18nService.getText("myheritage.plugin.merge.check.notvalidmessage.summary",
                        mergeCommitMsgSummaryText);
                String detailedMsg = i18nService.getText("myheritage.plugin.merge.check.notvalidmessage.detailed",
                        mergeCommitMsgDetailedText);
                return RepositoryHookResult.rejected(summaryMsg, detailedMsg);
            }
        }
        return RepositoryHookResult.accepted();
    }
}