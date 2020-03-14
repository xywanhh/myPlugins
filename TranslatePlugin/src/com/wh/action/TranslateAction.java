package com.wh.action;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import javafx.application.Application;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;

import java.awt.*;
import java.io.IOException;

public class TranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        // 获取当前用户所在的编辑器对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        // 通过编辑器对象获取用户选择对象
        SelectionModel selectionModel = editor.getSelectionModel();
        // 获取选择对象选中的文本
        String text = selectionModel.getSelectedText();

        if (StringUtils.isEmpty(text)) {
            return;
        }
        // http://translate.google.cn/translate_a/single?client=gtx&dt=t&dj=1&ie=UTF-8&sl=auto&tl=zh_TW&q=calculate
        // http://fanyi.youdao.com/openapi.do?keyfrom=neverland&key=969918857&type=data&doctype=json&version=1.1&q=good
        final Gson gson = new Gson();

        String urlBaidu = "http://fanyi.youdao.com/openapi.do?keyfrom=neverland&key=969918857&type=data&doctype=json&version=1.1&q=%s";
        String url = String.format(urlBaidu, text);
        OkHttpClient okHttpClient = new OkHttpClient(); // 创建OkHttpClient对象
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        okHttpClient.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                String str = response.body().string();
                Translation translation = gson.fromJson(str, Translation.class);
                System.out.println(str);
                // 解析返回结果
                // 展示结果
                // Messages.showMessageDialog(project, translation.toString(), "翻译结果", null);
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // 展示1
                        // Messages.showMessageDialog(project, translation.toString(), "翻译结果", null);
                        // 展示2
                        JBPopupFactory factory = JBPopupFactory.getInstance();
                        BalloonBuilder builder = factory.createHtmlTextBalloonBuilder(str, null,
                                new JBColor(new Color(188, 238, 188), new Color(73, 120, 73)),
                                null);
                        builder.setFadeoutTime(5000) // 无操作5s后隐藏
                                .createBalloon() // 创建气泡
                                .show(factory.guessBestPopupLocation(editor), Balloon.Position.below); // 制定位置并显示在下方
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                System.out.println(e.getMessage());
            }
        });


    }
}
