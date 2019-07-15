package com.myheritage.bitbucket.merge.checks;

import com.atlassian.bitbucket.scope.Scope;
import com.atlassian.bitbucket.setting.Settings;
import com.atlassian.bitbucket.setting.SettingsValidationErrors;
import com.atlassian.bitbucket.setting.SettingsValidator;
import javax.annotation.Nonnull;

public class IsValidCommitMessageSettingsValidator implements SettingsValidator {

    @Override
    public void validate(@Nonnull Settings settings, @Nonnull SettingsValidationErrors errors, @Nonnull Scope scope) {
        // Check that there is a value in the Regex field
        if (settings.getString("mergeCommitMsgRegex").trim().isEmpty()) {
            errors.addFieldError("mergeCommitMsgRegex", "A Regex is required");
        }

        // Check that the summary notification is not empty
        if (settings.getString("mergeCommitMsgSummaryText").trim().isEmpty()) {
            errors.addFieldError("mergeCommitMsgSummaryText", "A summary notification message is required");
        }

        // Check that the detailed notification is not empty
        if (settings.getString("mergeCommitMsgDetailedText").trim().isEmpty()) {
            errors.addFieldError("mergeCommitMsgDetailedText", "A detailed notification message is required");
        }
    }
}