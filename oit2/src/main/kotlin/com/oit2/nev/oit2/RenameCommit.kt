package com.oit2.nev.oit2

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import git4idea.commands.Git
import git4idea.commands.GitCommand
import git4idea.commands.GitLineHandler
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager


class RenameCommit : AnAction("Rename Current Commit") {

    override fun update(event: AnActionEvent) {}

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        val repository: GitRepository? = GitRepositoryManager.getInstance(project).repositories.firstOrNull()

        if (repository == null) {
            Messages.showErrorDialog(project, "Repository not initialized", "Error")
            return
        }

        val newMessage = Messages.showInputDialog(project, "Enter new commit message",
            "Rename Last Commit", Messages.getQuestionIcon()
        ) ?: return

        val handler = GitLineHandler(project, repository.root, GitCommand.COMMIT)
        handler.setSilent(false)
        handler.addParameters("--amend", "-m", newMessage)

        val result = Git.getInstance().runCommand(handler)
        if (result.success()) {
            Messages.showInfoMessage(project, "Commit renamed", "Success")
            repository.update()
        } else {
            Messages.showErrorDialog(project, "Failed to rename commit", "Error")
        }
    }
}
