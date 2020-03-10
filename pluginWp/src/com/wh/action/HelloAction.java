package com.wh.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HelloAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        // 获取得到当前工程对象
        Project project = e.getData(PlatformDataKeys.PROJECT);

        String name = Messages.showInputDialog(project, "Your Name:", "Please Input:", Messages.getQuestionIcon());
        Messages.showMessageDialog("Hello " + name, "Hello Dialog", Messages.getInformationIcon());
    }
}
