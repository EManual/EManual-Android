package io.github.emanual.app.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.emanual.app.entity.InterviewJSONEntity;
import io.github.emanual.app.entity.QuestionEntity;

/**
 * Author: jayin
 * Date: 2/25/16
 */
public class InterviewResource {
    /**
     * 获取所有interviews/* 所有文件名(英文名)列表
     * @param context
     * @return
     */
    public static List<String> getInterviewNameList(Context context) {
        List<String> books = new ArrayList<>();
        File bookDir = new File(AppPath.getInterviewsPath(context));
        if (!bookDir.exists()) {
            bookDir.mkdirs();
        }
        books.addAll(Arrays.asList(bookDir.list()));
        return books;
    }

    /**
     * 获取所有interviews/<name>/interview/interview.json
     * @param context
     * @return
     */
    public static List<InterviewJSONEntity> getInterviewJSONList(Context context) {
        List<InterviewJSONEntity> interviewJSONEntities = new ArrayList<>();
        File interviewDir = new File(AppPath.getInterviewsPath(context));
        if (!interviewDir.exists()) {
            interviewDir.mkdirs();
        }
        for (String interviewName : interviewDir.list()) {
            String json = _.readFile(AppPath.getInterviewJSONFilePath(context, interviewName));
            InterviewJSONEntity interviewJSONEntity = InterviewJSONEntity.createByJSON(json, InterviewJSONEntity.class);
            interviewJSONEntities.add(interviewJSONEntity);
        }
        return interviewJSONEntities;
    }

    /**
     * 获取Questions
     * 获取所有interviews/<name>/interview/questions/*.json
     * @param context
     * @param interviewName
     * @return
     */
    public static List<QuestionEntity> getQuestionList(Context context, String interviewName){
        List<QuestionEntity> result = new ArrayList<>();
        File questionsDir = new File(AppPath.getInterviewQuestionsRootPath(context, interviewName));
        if(!questionsDir.exists()){
            return result;
        }
        for(String questionsFileName : questionsDir.list()){
            String json  = _.readFile(questionsDir.getAbsolutePath()+"/"+questionsFileName);
            QuestionEntity questionEntity = QuestionEntity.createByJSON(json, QuestionEntity.class);
            result.add(questionEntity);
        }
        return result;
    }

}
